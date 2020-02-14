package com.guce.chain.executor;

import com.google.common.base.Stopwatch;
import com.guce.chain.IChainService;
import com.guce.chain.anno.ChainService;
import com.guce.chain.model.ChainExecServiceWrapper;
import com.guce.chain.model.ChainRequest;
import com.guce.chain.model.ChainResponse;
import com.guce.exception.ChainException;
import com.guce.exception.ChainRollbackException;
import com.guce.spring.SpringContextBean;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author chengen.gu
 * @DATE 2020/2/13 2:07 下午
 */
@Component
public class ChainExecutor {

    private static Logger logger = LoggerFactory.getLogger(ChainExecutor.class);

    private static Map<String, List<ChainExecServiceWrapper>> chainExecutorMap = new ConcurrentHashMap<>(32);

    private static final AtomicBoolean LOADER_FINISHED = new AtomicBoolean(false);

    private volatile static int DEFAULT_CHAIN_SERVICE_LIST_CAPACITY = 8;

    private static Comparator<ChainExecServiceWrapper> rankChainServices =
            Comparator.comparingInt(ChainExecServiceWrapper::getOrder);

    /**
     * lazy加载
     */
    private void init(){

        if (LOADER_FINISHED.get()){
            return ;
        }

        if (!LOADER_FINISHED.get()){

            synchronized (LOADER_FINISHED){

                if ( !LOADER_FINISHED.get()){

                    initChain();
                    LOADER_FINISHED.set(true);
                }
            }
        }
    }

    public void execute(String chainResourceName , ChainRequest request , ChainResponse response){

        init();

        List<ChainExecServiceWrapper> chainServiceList = chainExecutorMap.get(chainResourceName);

        if (CollectionUtils.isEmpty(chainServiceList)){

            throw new ChainException("没有相关 chainService chainResouceName : " + chainResourceName);
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

            boolean doNext = doService(chainService,request,response);
            if ( !doNext ){
                break;
            }
        }

        if ( !CollectionUtils.isEmpty(futureList) ){
            CompletableFuture[] arr = futureList.toArray(new CompletableFuture[0]);
            CompletableFuture future = CompletableFuture.allOf(arr);
            try {
                future.get(maxAsyncTimeout,TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {

                logger.error("chain service async exception;",e);
                if (e.getCause() instanceof ChainRollbackException){
                    rollback = true;
                }
            }
        }

        if ( rollback ){
            logger.warn("chain service do roll back start...");
            while (servcieStack.size() > 0){
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

    private void initChain(){

        Stopwatch watch = Stopwatch.createStarted();
        try{
            logger.warn("========chain services init start...");
            String[] name = SpringContextBean.getBeanNamesForType(IChainService.class);

            for (int i = 0 ; i < name.length ; i++ ){
                IChainService service = SpringContextBean.getBean(name[i]);
                ChainService annoService = service.getClass().getAnnotation(ChainService.class);

                if (annoService == null){
                    continue;
                }

                ChainExecServiceWrapper serviceWrapper = new ChainExecServiceWrapper();
                serviceWrapper.setChainService(service);
                serviceWrapper.annoParamWrapper(annoService);

                String chainResourceName = annoService.value();
                List<ChainExecServiceWrapper> list = chainExecutorMap
                        .computeIfAbsent(chainResourceName , k -> new ArrayList<>(DEFAULT_CHAIN_SERVICE_LIST_CAPACITY));

                list.add(serviceWrapper);
            }

            logger.warn("========chain services init rank compose ...");
            for ( Map.Entry<String,List<ChainExecServiceWrapper>> entry : chainExecutorMap.entrySet()){

                List<ChainExecServiceWrapper> chainServiceList = entry.getValue();
                if (CollectionUtils.isNotEmpty(chainServiceList)){
                    chainServiceList.sort(rankChainServices);
                    logger.warn("chain service name:{}  sort; chain service :{}",entry.getKey(),chainServiceList);

                }
            }

            logger.warn("========chain serivces init end ; cost time :{}",watch.elapsed(TimeUnit.MILLISECONDS));
        }catch (Exception e){
            logger.error("init chain service error:",e);
        }finally {
            watch.stop();
        }


    }

}
