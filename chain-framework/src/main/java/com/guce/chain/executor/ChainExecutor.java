package com.guce.chain.executor;

import com.google.common.base.Stopwatch;
import com.guce.chain.IChainService;
import com.guce.chain.anno.ChainSerivce;
import com.guce.chain.model.ChainRequest;
import com.guce.chain.model.ChainResponse;
import com.guce.spring.SpringContextBean;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author chengen.gu
 * @DATE 2020/2/13 2:07 下午
 */
@Component
public class ChainExecutor {

    private static Logger logger = LoggerFactory.getLogger(ChainExecutor.class);

    private Map<String, List<IChainService>> chainExecutorMap = new ConcurrentHashMap<>(32);

    private static volatile AtomicBoolean loaderFinished = new AtomicBoolean(false);

    private static Comparator<IChainService> rankChainServices = (ch1, ch2) -> {

        ChainSerivce ann1 = ch1.getClass().getAnnotation(ChainSerivce.class);
        ChainSerivce ann2 = ch2.getClass().getAnnotation(ChainSerivce.class);

        return ann1.order() - ann2.order() ;
    };
    private void init(){

        if (loaderFinished.get()){
            return ;
        }
        //todo 责任链加载各个责任
        if (!loaderFinished.get()){

            synchronized (loaderFinished){

                if ( !loaderFinished.get()){

                    initChain();
                    loaderFinished.set(true);
                }
            }
        }
    }

    public void execute(String chainResourceName , ChainRequest request , ChainResponse response){

        init();

        List<IChainService> chainServiceList = chainExecutorMap.get(chainResourceName);

        for (IChainService service : chainServiceList ){

            try{
                service.handle(request,response);
            }catch (Throwable th){
                service.handleException(request,response,th);

            }finally {
                service.doComplated(request,response);
            }
        }


    }

    private void initChain(){

        Stopwatch watch = Stopwatch.createStarted();
        try{
            logger.warn("========chain services init start...");
            String[] name = SpringContextBean.getBeanNamesForType(IChainService.class);

            for (int i = 0 ; i < name.length ; i++ ){
                IChainService service = SpringContextBean.getBean(name[i]);
                ChainSerivce annoService = service.getClass().getAnnotation(ChainSerivce.class);

                if (annoService == null){
                    continue;
                }

                String chainResourceName = annoService.value();
                List<IChainService> list = chainExecutorMap
                        .computeIfAbsent(chainResourceName , k -> new ArrayList<>(8));

                list.add(service);
            }

            logger.warn("========chain services init rank compose ...");
            for ( Map.Entry<String,List<IChainService>> entry : chainExecutorMap.entrySet()){

                List<IChainService> chainServiceList = entry.getValue();
                if (CollectionUtils.isNotEmpty(chainServiceList)){
                    logger.warn("chain service name:{}  sort; chain service num:{}",entry.getKey(),chainServiceList.size());
                    chainServiceList.sort(rankChainServices);
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
