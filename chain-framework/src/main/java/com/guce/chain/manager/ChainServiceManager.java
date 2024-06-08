package com.guce.chain.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.guce.chain.IChainService;
import com.guce.chain.anno.ChainService;
import com.guce.chain.model.ChainExecServiceWrapper;
import com.guce.spring.listener.ChainSpringApplicationListener;
import com.guce.spring.util.SpringContextBean;
import com.guce.utils.ClassUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
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
public class ChainServiceManager implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(ChainServiceManager.class);

    private static Map<String, List<ChainExecServiceWrapper>> chainExecutorMap = new ConcurrentHashMap<>(32);

    private static final AtomicBoolean LOADER_FINISHED = new AtomicBoolean(false);

    private static final int DEFAULT_CHAIN_SERVICE_LIST_CAPACITY = 8;

    private static final String FLOW_LIST_CONFIG_FILE = "flow_conf.factories";

    private final static Comparator<ChainExecServiceWrapper> RANK_CHAIN_SERVICES =
            Comparator.comparingInt(ChainExecServiceWrapper::getOrder);

    private final static String FILE_FILTER_SUFFIX = "Flow.json";

    private final static long SPRING_LOADER_UNFINISHED_SLEEP_MILL = 500;

    public ChainServiceManager() {
        logger.info("==========ChainServiceManager init start==========");
    }

    /**
     * lazy加载
     */
    public void init() {

        if (LOADER_FINISHED.get()) {
            return;
        }
        while (!ChainSpringApplicationListener.isSpringLoaderFinished()){
            try {
                logger.info("spring 加载未完成，休眠 {} ms." , SPRING_LOADER_UNFINISHED_SLEEP_MILL);
                Thread.sleep(SPRING_LOADER_UNFINISHED_SLEEP_MILL);
            } catch (InterruptedException e) {
                logger.error("spring加载未完成，休眠异常;" ,e);
            }
        }

        if (!LOADER_FINISHED.get()) {
            synchronized (LOADER_FINISHED) {

                if (!LOADER_FINISHED.get()) {
                    initChain();
                    LOADER_FINISHED.set(true);
                }
            }
        }
    }

    public List<ChainExecServiceWrapper> getChainServiceInfoList(String resourceName) {
        this.init();
        return chainExecutorMap.get(resourceName);
    }

    private void initChain() {

        Stopwatch watch = Stopwatch.createStarted();
        try {
            logger.warn("========chain services init start...");

            //loader annotation flow service
            List<ChainExecServiceWrapper> springFlowList = initLoaderAnnotationFlowNodeInfo();

            // loader config file flow service
            List<ChainExecServiceWrapper> fileChainServiceList = initLoadFileFlowNodeInfo();

            if (CollectionUtils.isNotEmpty(fileChainServiceList)) {
                springFlowList.addAll(fileChainServiceList);
            }

            long sortStart = System.currentTimeMillis();
            logger.warn("========chain services flow node sort  start...");
            springFlowList.sort(RANK_CHAIN_SERVICES);

            long rangeComposeStart = System.currentTimeMillis();
            logger.warn("========chain services flow node sort  end... cost time:{}", (rangeComposeStart - sortStart));

            logger.warn("========chain services init rank compose start ...");
            Map<String, List<ChainExecServiceWrapper>> chainExecMap = new ConcurrentHashMap<>(16);

            springFlowList.forEach(chainExecServiceWrapper -> {
                String resourceName = chainExecServiceWrapper.getChainResourceName();
                List<ChainExecServiceWrapper> chainList = chainExecMap
                        .computeIfAbsent(resourceName, k -> new ArrayList<>(DEFAULT_CHAIN_SERVICE_LIST_CAPACITY));
                chainList.add(chainExecServiceWrapper);

            });
            logger.warn("========chain services init rank compose end ... cost time:{} ms."
                    , (System.currentTimeMillis() - rangeComposeStart));

            chainExecutorMap = chainExecMap;
            logger.warn("========chain Executor flow service Map info:{}", chainExecutorMap);

            logger.warn("========chain serivces init end ; cost time :{}", watch.elapsed(TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            logger.error("init chain service error:", e);
        } finally {
            watch.stop();
        }


    }

    /**
     * 加载配置注解的流程节点
     *
     * @return
     */
    private List<ChainExecServiceWrapper> initLoaderAnnotationFlowNodeInfo() {

        logger.warn("######chain service loader annotation flow node start...");
        List<ChainExecServiceWrapper> flowResultList = new ArrayList<>();
        String[] names = SpringContextBean.getBeanNamesForType(IChainService.class);
        StringBuffer sb = new StringBuffer();
        Arrays.stream(names).forEach(name -> sb.append(name).append("|"));
        logger.warn("spring init chain service beanNames:{}", sb.toString());
        if (names != null && names.length > 0) {

            Arrays.stream(names).forEach(serviceName -> {

                IChainService service = SpringContextBean.getBean(serviceName);

                ChainService annoService = service.getClass().getAnnotation(ChainService.class);

                if (annoService == null) {
                    logger.warn("init chain service no annotation:{} ; service:{} ;有可能在配置文件中", serviceName
                            , service.getClass().getCanonicalName());
                    return;
                }

                ChainExecServiceWrapper serviceWrapper = new ChainExecServiceWrapper();
                serviceWrapper.setChainService(service);
                serviceWrapper.annoParamWrapper(annoService);

                flowResultList.add(serviceWrapper);
            });
        }
        logger.warn("######chain service loader annotation flow node end... and loader flow resource:{}", flowResultList);

        return flowResultList;
    }

    /**
     * 初始化加载 后缀为 Flow.json的文件
     *
     * @return
     */
    public static List<ChainExecServiceWrapper> initLoadFileFlowNodeInfo() {

        Stopwatch watch = Stopwatch.createStarted();
        logger.info("######Chain Service loader flow config file info start...");
        List<ChainExecServiceWrapper> result = new ArrayList<>();
        try {
            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            File file = new File(rootPath + File.separator + "node");
            if (file.exists() && file.isDirectory()) {

                Collection<File> listFiles = FileUtils.listFiles(file,
                        FileFilterUtils.suffixFileFilter(FILE_FILTER_SUFFIX), null);
                logger.info("loader suffix file : {} ; size: {} ", FILE_FILTER_SUFFIX, listFiles.size());
                if (CollectionUtils.isNotEmpty(listFiles)) {

                    listFiles.forEach(flowFile -> {

                        List<ChainExecServiceWrapper> flowList = loaderFlowFileConfigInfo(flowFile);
                        if (CollectionUtils.isNotEmpty(flowList)) {
                            result.addAll(flowList);
                        }

                    });
                }

            }
            logger.info("loader suffix file : {} ;end cost time : {}  ", FILE_FILTER_SUFFIX, watch.elapsed(TimeUnit.MILLISECONDS));


            ///// 加载指定文件  flow_chain_cfg.flow
            logger.info("loader file start : {} ", FLOW_LIST_CONFIG_FILE);
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            Enumeration<URL> urls = (classLoader != null ?
                    classLoader.getResources(FLOW_LIST_CONFIG_FILE) :
                    ClassLoader.getSystemResources(FLOW_LIST_CONFIG_FILE));

            while (urls.hasMoreElements()) {
                String url = urls.nextElement().getPath();
                UrlResource resource = new UrlResource(url);
                //InputStream inputStream = resource.getInputStream();
                String content = IOUtils.toString(resource.getURI(), "UTF-8");
                List<ChainExecServiceWrapper> flowList = loaderFlowFileConfigInfo(content);
                logger.info("######Chain service add jar path: {} ; flowlist: {}", url, flowList);
                if (CollectionUtils.isEmpty(flowList)) {
                    continue;
                }
                result.addAll(flowList);

            }
            logger.info("######Chain Service loader flow config file info start... and cost time:{}", watch.elapsed(TimeUnit.MILLISECONDS));

        } catch (Exception e) {
            logger.error("init flow service from config file error;", e);
        } finally {
            watch.stop();
        }
        return result;
    }

    private static List<ChainExecServiceWrapper> loaderFlowFileConfigInfo(File flowFile) {

        List<ChainExecServiceWrapper> result = new ArrayList<>();
        try {
            long start = System.currentTimeMillis();
            logger.warn("loader flow config file  start :{} ", flowFile.getAbsolutePath());
            String context = FileUtils.readFileToString(flowFile, "UTF-8");
            if (!StringUtils.isEmpty(context)) {
                result = loaderFlowFileConfigInfo(context);
            }
            logger.warn("loader flow config file  end :{} ; cost time:{} "
                    , flowFile.getAbsolutePath(), (System.currentTimeMillis() - start));
        } catch (IOException e) {
            logger.error("chain flow node init reader file error ; file:{}", flowFile.getAbsolutePath());
        }
        return result;
    }

    private static List<ChainExecServiceWrapper> loaderFlowFileConfigInfo(String context) {

        List<ChainExecServiceWrapper> result = new ArrayList<>();

        if (!StringUtils.isEmpty(context)) {
            JSONObject jsonObject = JSON.parseObject(context);

            jsonObject.forEach((key, value) -> {

                if (value instanceof JSONArray) {
                    JSONArray jsonArr = (JSONArray) value;
                    List<ChainExecServiceWrapper> serviceWrapperList =
                            jsonArr.toJavaList(ChainExecServiceWrapper.class);

                    if (CollectionUtils.isNotEmpty(serviceWrapperList)) {
                        /**
                         * 某个流程节点配置有问题时，删除该流程节点
                         */
                        List<ChainExecServiceWrapper> removeList = new ArrayList<>();

                        /**
                         * 如果某个流程节点不存在且 是必须执行的节点 那此时就需要删除该流程 不让其往下执行
                         */
                        AtomicBoolean addToExecutorFLow = new AtomicBoolean(true);

                        serviceWrapperList.forEach(chainExecServiceWrapper -> {

                            chainExecServiceWrapper.setChainResourceName(key);
                            String servicePath = chainExecServiceWrapper.getServicePath();
                            if (!StringUtils.isEmpty(servicePath)) {
                                try {
                                    IChainService chainService = loaderChainService(servicePath);
                                    chainExecServiceWrapper.setChainService(chainService);
                                } catch (ClassNotFoundException e) {

                                    StringBuilder logSb = new StringBuilder();
                                    logSb.append("######加载流程：").append(key)
                                            .append("; 找不到流程节点：").append(servicePath);
                                    if (chainExecServiceWrapper.isNeedNode()) {

                                        logSb.append("；在该流程中此节点为必须节点，流程将不纳入可执行流程中！！");
                                        addToExecutorFLow.set(false);
                                    } else {
                                        logSb.append("；该流程节点不是必须节点，remove该流程节点！！");
                                        removeList.add(chainExecServiceWrapper);
                                    }
                                    logger.error(logSb.toString());
                                }
                            }
                        });

                        if (CollectionUtils.isNotEmpty(removeList)) {
                            serviceWrapperList.removeAll(removeList);
                        }

                        if (addToExecutorFLow.get() && CollectionUtils.isNotEmpty(serviceWrapperList)) {
                            result.addAll(serviceWrapperList);
                        }
                    }
                }
            });

        }
        return result;
    }

    public static <T> T loaderChainService(String classpath) throws ClassNotFoundException {

        Class<?> clazz = ClassUtils.getClassLoder(ChainServiceManager.class).loadClass(classpath);
        return (T) SpringContextBean.getBean(clazz);

    }


    private static Map<String, List<ChainExecServiceWrapper>> chainExecutorSpringMap = new ConcurrentHashMap<>(32);

    public static void chainNodeJoinFlow(ChainExecServiceWrapper chainExecServiceWrapper) {

        String resourceName = chainExecServiceWrapper.getChainResourceName();
        List<ChainExecServiceWrapper> chainList = chainExecutorSpringMap
                .computeIfAbsent(resourceName, key -> new ArrayList<>());
        chainList.add(chainExecServiceWrapper);
        chainList.sort(RANK_CHAIN_SERVICES);

    }

    private static List<ChainExecServiceWrapper> chainList = new ArrayList<>();

    public static List<ChainExecServiceWrapper> getChainList() {
        return chainList;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("chain list: {}" , chainList.size());
        chainList.stream().forEach( chain -> {
            String resourceName = chain.getChainResourceName();
            List<ChainExecServiceWrapper> innerChainList = chainExecutorSpringMap
                    .computeIfAbsent(resourceName, key -> new ArrayList<>());
            innerChainList.add(chain);
        });
        chainExecutorSpringMap.entrySet().stream().forEach(entry -> {
            List<ChainExecServiceWrapper> chainList = entry.getValue();
            chainList.sort(RANK_CHAIN_SERVICES);
        });
    }
}
