package com.guce.chain.executor;

import com.guce.chain.IChainService;
import com.guce.chain.manager.ChainServiceManager;
import com.guce.chain.model.ChainExecServiceWrapper;
import com.guce.chain.model.ChainRequest;
import com.guce.chain.model.ChainResponse;
import com.guce.exception.ChainException;
import com.guce.exception.ChainRollbackException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @Author chengen.gu
 * @DATE 2020/2/13 2:07 下午
 */
@Component
public class ChainExecutor {

    private static Logger logger = LoggerFactory.getLogger(ChainExecutor.class);

    private ChainServiceManager chainServiceManager;

    @Autowired
    public ChainExecutor(ChainServiceManager manager){
        this.chainServiceManager = manager;
    }

    public void execute(String chainResourceName , ChainRequest request , ChainResponse response){

        List<ChainExecServiceWrapper> chainServiceList = chainServiceManager.getChainServiceInfoList(chainResourceName);

        if (CollectionUtils.isEmpty(chainServiceList)){

            throw new ChainException("#######chain service 没有相关流程 chainResouceName : " + chainResourceName);
        }

        Stack<IChainService> servcieStack = new Stack<>();
        boolean rollback = false;
        List<CompletableFuture> futureList = new ArrayList<>(4);
        long maxAsyncTimeout = 0 ;
        for (ChainExecServiceWrapper service : chainServiceList ){
            IChainService chainService = service.getChainService();
            servcieStack.push(service.getChainService());

            boolean isAsync = service.isAsync();

            if (isAsync){
                long asyncTimeout = service.getAsyncTimeout();
                if (maxAsyncTimeout < asyncTimeout){
                    maxAsyncTimeout = asyncTimeout;
                }
                CompletableFuture<Boolean> future = CompletableFuture.supplyAsync( () -> doService(chainService,request,response));
                futureList.add(future);
                continue;
            }

            /**
             * 后续还有节点的话 那么需要先将等到这个异步执行完毕才继续；
             */
            if ( CollectionUtils.isNotEmpty(futureList) ){
                CompletableFuture[] arr = futureList.toArray(new CompletableFuture[0]);
                CompletableFuture future = CompletableFuture.allOf(arr);
                try {
                    future.get(maxAsyncTimeout,TimeUnit.MILLISECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {

                    logger.error("chain service async exception;resourceName:{}; exception{}"
                            ,chainResourceName,e.getMessage());
                    if (e.getCause() instanceof ChainRollbackException){
                        rollback = true;
                    }
                    break;
                }finally {
                    futureList.clear();
                }
            }

            try{
                boolean doNext = doService(chainService,request,response);
                if ( !doNext ){
                    break;
                }
            }catch (Throwable th){

                logger.error("chain service sync exception;{}",th.getMessage());
                if ( th instanceof ChainRollbackException ){
                    rollback = true;
                }
                break;
            }

        }

        if ( CollectionUtils.isNotEmpty(futureList) ){
            CompletableFuture[] arr = futureList.toArray(new CompletableFuture[0]);
            CompletableFuture future = CompletableFuture.allOf(arr);
            try {
                future.get(maxAsyncTimeout,TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {

                logger.error("chain service async exception;resourceName:{}; exception{}"
                        ,chainResourceName,e.getMessage());
                if (e.getCause() instanceof ChainRollbackException){
                    rollback = true;
                }
            }finally {
                futureList.clear();
            }
        }

        if ( rollback ){
            logger.warn("chain service do roll back start...");
            while ( !servcieStack.isEmpty() ){
                IChainService service = servcieStack.pop() ;
                service.doRollback(request,response);
            }
            logger.warn("chain service do roll back end...");
        }

    }

    private static boolean doService(IChainService service ,ChainRequest request,ChainResponse response ){

        try{

            return service.handle(request,response);

        }catch (Throwable th){

            service.handleException(request,response,th);

            if (th instanceof ChainRollbackException){
                throw th;
            }
            return false;
        }finally {
            service.doComplated(request,response);
        }

    }
}
