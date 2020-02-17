package com.guce.chain.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.guce.chain.IChainService;
import com.guce.chain.anno.ChainService;
import com.guce.chain.model.ChainExecServiceWrapper;
import com.guce.spring.util.SpringContextBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author chengen.gu
 * @DATE 2020/2/17 2:26 下午
 */
@Component("chainServiceManager")
public class ChainServiceManager {

    private static Logger logger = LoggerFactory.getLogger(ChainServiceManager.class);

    private static Map<String, List<ChainExecServiceWrapper>> chainExecutorMap = new ConcurrentHashMap<>(32);

    private static final AtomicBoolean LOADER_FINISHED = new AtomicBoolean(false);

    private volatile static int DEFAULT_CHAIN_SERVICE_LIST_CAPACITY = 8;

    private static Comparator<ChainExecServiceWrapper> rankChainServices =
            Comparator.comparingInt(ChainExecServiceWrapper::getOrder);

    private static volatile String FILE_FILTER_SUFFIX = "Flow.json";

    /**
     * lazy加载
     */
    public void init(){

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

    public List<ChainExecServiceWrapper> getChainServiceInfoList(String resourceName){
        this.init();
        return chainExecutorMap.get(resourceName);
    }
    private void initChain(){

        Stopwatch watch = Stopwatch.createStarted();
        try{
            logger.warn("========chain services init start...");

            //loader annotation flow service
            List<ChainExecServiceWrapper> springFlowList = initLoaderAnnotationFlowNodeInfo();

            // loader config file flow service
            List<ChainExecServiceWrapper> fileChainServiceList = initLoadFileFlowNodeInfo();

            if (CollectionUtils.isNotEmpty(fileChainServiceList)){
                springFlowList.addAll(fileChainServiceList);
            }

            long sortStart = System.currentTimeMillis();
            logger.warn("========chain services flow node sort  start...");
            springFlowList.sort(rankChainServices);

            long rangeComposeStart = System.currentTimeMillis();
            logger.warn("========chain services flow node sort  end... cost time:{}",(rangeComposeStart - sortStart));

            logger.warn("========chain services init rank compose start ...");
            Map<String,List<ChainExecServiceWrapper>> chainExecMap = new ConcurrentHashMap<>(16);

            springFlowList.forEach(chainExecServiceWrapper -> {
                String resourceName = chainExecServiceWrapper.getChainResourceName();
                List<ChainExecServiceWrapper> chainList =chainExecMap
                        .computeIfAbsent(resourceName , k -> new ArrayList<>(DEFAULT_CHAIN_SERVICE_LIST_CAPACITY));
                chainList.add(chainExecServiceWrapper);

            });
            logger.warn("========chain services init rank compose end ... cost time:{} ms."
                    ,(System.currentTimeMillis() - rangeComposeStart));

            chainExecutorMap = chainExecMap;
            logger.warn("========chain Executor flow service Map info:{}",chainExecutorMap);

            logger.warn("========chain serivces init end ; cost time :{}",watch.elapsed(TimeUnit.MILLISECONDS));
        }catch (Exception e){
            logger.error("init chain service error:",e);
        }finally {
            watch.stop();
        }


    }

    /**
     * 加载配置注解的流程节点
     * @return
     */
    private List<ChainExecServiceWrapper> initLoaderAnnotationFlowNodeInfo(){

        logger.warn("######chain service loader annotation flow node start...");
        List<ChainExecServiceWrapper> flowResultList = new ArrayList<>();
        String[] name = SpringContextBean.getBeanNamesForType(IChainService.class);
        if (name != null && name.length > 0){

            Arrays.stream(name).forEach(serviceName -> {

                IChainService service = SpringContextBean.getBean(serviceName);

                ChainService annoService = service.getClass().getAnnotation(ChainService.class);

                if (annoService == null){
                    logger.warn("init chain service no annotation:{} ; service:{} ;有可能在配置文件中",serviceName
                            ,service.getClass().getCanonicalName());
                    return ;
                }

                ChainExecServiceWrapper serviceWrapper = new ChainExecServiceWrapper();
                serviceWrapper.setChainService(service);
                serviceWrapper.annoParamWrapper(annoService);

                flowResultList.add(serviceWrapper);
            });
        }
        logger.warn("######chain service loader annotation flow node end...");

        return flowResultList;
    }

    /**
     * 初始化加载 后缀为 Flow.json的文件
     * @return
     */
    public static List<ChainExecServiceWrapper> initLoadFileFlowNodeInfo(){

        Stopwatch watch = Stopwatch.createStarted();
        logger.info("######Chain Service loader flow config file info start...");
        List<ChainExecServiceWrapper> result = new ArrayList<>();
        try{
            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            File file = new File(rootPath + File.separator + "node");
            if (file.exists() && file.isDirectory()){

                Collection<File> listFiles = FileUtils.listFiles( file,
                        FileFilterUtils.suffixFileFilter(FILE_FILTER_SUFFIX),null);

                if (CollectionUtils.isNotEmpty(listFiles)){

                    listFiles.forEach( flowFile -> {
                        List<ChainExecServiceWrapper> flowList = loaderFlowFileConfigInfo(flowFile);
                        if (CollectionUtils.isNotEmpty(flowList)){
                            result.addAll(flowList);
                        }

                    });
                }

            }
            logger.info("######Chain Service loader flow config file info start... and cost time:{}",watch.elapsed(TimeUnit.MILLISECONDS));

        }catch (Exception e){
            logger.error("init flow service from config file error;",e);
        }finally {
            watch.stop();
        }
        return result;
    }

    private static List<ChainExecServiceWrapper> loaderFlowFileConfigInfo(File flowFile){

        List<ChainExecServiceWrapper> result = new ArrayList<>();
        try {
            long start = System.currentTimeMillis();
            logger.warn("loader flow config file  start :{} ",flowFile.getAbsolutePath());
            String context = FileUtils.readFileToString(flowFile,"UTF-8");
            if (StringUtils.isNoneBlank(context)){
                JSONObject jsonObject = JSON.parseObject(context);

                jsonObject.forEach((key, value) -> {

                    if (value instanceof JSONArray){
                        JSONArray jsonArr = (JSONArray) value;
                        List<ChainExecServiceWrapper> serviceWrapperList =
                                jsonArr.toJavaList(ChainExecServiceWrapper.class);

                        if (CollectionUtils.isNotEmpty(serviceWrapperList)){
                            /**
                             * 某个流程节点配置有问题时，删除该流程节点
                             */
                            List<ChainExecServiceWrapper> removeList = new ArrayList<>();

                            /**
                             * 如果某个流程节点不存在且 是必须执行的节点 那此时就需要删除该流程 不让其往下执行
                             */
                            AtomicBoolean addToExecutorFLow = new AtomicBoolean(true);

                            serviceWrapperList.forEach( chainExecServiceWrapper -> {
                                chainExecServiceWrapper.setChainResourceName(key);
                                String servicePath = chainExecServiceWrapper.getServicePath();
                                if (StringUtils.isNoneBlank(servicePath)){
                                    try {
                                        Class clazz = Thread.currentThread().getContextClassLoader().loadClass(servicePath);
                                        IChainService chainService = (IChainService) SpringContextBean.getBean(clazz);
                                        chainExecServiceWrapper.setChainService(chainService);
                                    } catch (ClassNotFoundException e) {

                                        StringBuilder logSb = new StringBuilder();
                                        logSb.append("######执行流程：").append(key)
                                                .append("; 找不到流程节点：").append(servicePath);
                                        if (chainExecServiceWrapper.isNeedNode()){

                                            logSb.append("；此流程将不纳入可执行流程中！！");
                                            addToExecutorFLow.set(false);
                                        }else {
                                            logSb.append("；该流程节点不是必须节点，remove该流程节点！！");
                                            removeList.add(chainExecServiceWrapper);
                                        }
                                        logger.error(logSb.toString());
                                    }
                                }
                            });

                            if (CollectionUtils.isNotEmpty(removeList)){
                                serviceWrapperList.removeAll(removeList);
                            }

                            if (addToExecutorFLow.get() && CollectionUtils.isNotEmpty(serviceWrapperList)){
                                result.addAll(serviceWrapperList);
                            }
                        }
                    }
                });

            }
            logger.warn("loader flow config file  end :{} ; cost time:{} "
                    ,flowFile.getAbsolutePath(),(System.currentTimeMillis() - start));
        } catch (IOException e) {
            logger.error("chain flow node init reader file error ; file:{}",flowFile.getAbsolutePath());
        }
        return result;
    }

}
