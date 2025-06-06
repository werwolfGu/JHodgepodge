package com.guce.allocation;

import com.guce.allocation.manager.ClusterDispatchManager;
import com.guce.allocation.manager.ClusterThreadDispatcherManager;
import com.guce.allocation.manager.IClusterConsumerThreadService;
import com.guce.loadbalance.LoadBalance;
import com.guce.loadbalance.impl.ConsistentHashLoadBalance;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Author chengen.gce
 * @DATE 2022/5/13 23:04
 */
public class ClusterAllocationBuilder {

    private LoadBalance serverLoadBalance;
    private LoadBalance threadLoadBalance;
    private final static LoadBalance DEFAULT_LOADBALANCE = new ConsistentHashLoadBalance();

    private final static int DEFAULT_BATCH_SIZE = 1;
    private final static int DEFAULT_CONCURRENT_THREAD_NUMBER = 4;
    private String businessName;

    private int threadConcurrentNumber = 1;
    private Function<List<TradeDataEntity>,Boolean> businessInvoker;
    private int batchSize ;
    private Supplier<Boolean> interruptedBusinessThreadLoopInvoker;

    private ExecutorService businessHandleExecutor;
    private ExecutorService msgPollExecutor;

    private BiFunction<Map<String,Object> , IClusterConsumerThreadService, Boolean> msgPollInvoker ;
    private static final Map<String,ExecutorService> businessThreadMap =
            new ConcurrentHashMap<>(128);
    /**
     * 是否全量数据拉去模式，
     * true：全量数据拉去的话，会对消息在服务器层面先做负载均衡分配；
     * false : 消息在线程层面做负载均衡
     */
    private boolean allMsgPollMode = false;


    private ClusterDispatchManager clusterAllocationManager;
    private ClusterThreadDispatcherManager threadsManager;

    private final static String DEFAULT_THREAD_NAME_PRE = "cluster-allocation-";
    private final static AtomicInteger THREAD_NUMBER = new AtomicInteger(0);

    public static ExecutorService createDefaultThreadPoolExecutor(String businessName) {

        int CORE_CPU_NUM = Runtime.getRuntime().availableProcessors() + 1;
        int maxThreadNum = CORE_CPU_NUM * 2;
        return businessThreadMap.computeIfAbsent( businessName , key ->
                new ThreadPoolExecutor(CORE_CPU_NUM,maxThreadNum , 180, TimeUnit.SECONDS
                , new LinkedBlockingQueue<>(10000), (r) -> {
                    String threadName = DEFAULT_THREAD_NAME_PRE + businessName + "-" + THREAD_NUMBER.getAndIncrement();
                    Thread res = new Thread(r,threadName);
                    return res;
        }));

    }


    public static ClusterAllocationBuilder builder() {
        ClusterAllocationBuilder builder = new ClusterAllocationBuilder();
        return builder;
    }

    public ClusterAllocationBuilder build(){

        if (StringUtils.isEmpty(businessName)){
            throw new IllegalArgumentException("缺少业务参数 business");
        }

        if (businessInvoker == null) {
            throw new IllegalArgumentException("缺少业务逻辑实现 businessInvoker");
        }
        serverLoadBalance = Optional.ofNullable(serverLoadBalance).orElse(DEFAULT_LOADBALANCE);
        threadLoadBalance = Optional.ofNullable(threadLoadBalance).orElse(DEFAULT_LOADBALANCE);

        businessHandleExecutor = Optional.ofNullable(businessHandleExecutor)
                .orElseGet( () -> createDefaultThreadPoolExecutor(businessName));

        msgPollExecutor = Optional.ofNullable(msgPollExecutor)
                .orElseGet( () -> createDefaultThreadPoolExecutor(businessName));

        batchSize = Optional.ofNullable(batchSize).orElse(DEFAULT_BATCH_SIZE);
        threadConcurrentNumber = Optional.ofNullable(threadConcurrentNumber).orElse(DEFAULT_CONCURRENT_THREAD_NUMBER);

        clusterAllocationManager
                = new ClusterDispatchManager(allMsgPollMode,serverLoadBalance);

        threadsManager = new ClusterThreadDispatcherManager(businessName);
        threadsManager.setClusterAllocationManager(clusterAllocationManager);
        threadsManager.setBatchSize(batchSize);
        threadsManager.setBusinessConsumeExecutor(businessHandleExecutor);
        threadsManager.setMsgPollExecutor(msgPollExecutor);
        threadsManager.setThreadConcurrentNumber(threadConcurrentNumber);
        threadsManager.setLoadBalance(threadLoadBalance);
        threadsManager.setBusinessInvoker(businessInvoker);
        threadsManager.setInterruptedLoopInvoker(interruptedBusinessThreadLoopInvoker);
        return this;
    }
    /**
     * 创建集群分配管理类
     */
    public ClusterDispatchManager buildClusterAllocation(){
        return clusterAllocationManager;
    }

    public IClusterConsumerThreadService buildClusterConsumerThreadsManager() {

        return threadsManager;
    }

    public CompletableFuture startupMsgPollService(Map<String,Object> param){
       return  threadsManager.startupBusinessMsgPoll(param, msgPollInvoker);
    }

    /**
     * 业务类型
     * @param businessName
     * @return
     */
    public ClusterAllocationBuilder businessName(String businessName) {
        this.businessName = businessName;
        return this;
    }
    public ClusterAllocationBuilder interruptedBusinessThreadLoopInvoker(Supplier<Boolean> interruptedBusinessThreadLoopInvoker) {
        this.interruptedBusinessThreadLoopInvoker = interruptedBusinessThreadLoopInvoker;
        return this;
    }

    /**
     * 负载均衡算法
     * @param threadLoadBalance
     * @return
     */
    public ClusterAllocationBuilder threadLoadBalance(LoadBalance threadLoadBalance) {
        this.threadLoadBalance = threadLoadBalance;
        return this;
    }

    public ClusterAllocationBuilder serverLoadBalance(LoadBalance serverLoadBalance) {
        this.serverLoadBalance = serverLoadBalance;
        return this;
    }

    public ClusterAllocationBuilder allMsgPollMode(boolean allMsgPollMode) {
        this.allMsgPollMode = allMsgPollMode;
        return this;
    }

    /**
     * 并发线程数
     * @param threadConcurrentNumber
     * @return
     */
    public ClusterAllocationBuilder threadConcurrentNumber(int threadConcurrentNumber){
        this.threadConcurrentNumber = threadConcurrentNumber;
        return this;
    }

    /**
     * 业务代码函数
     * @param businessInvoker
     * @return
     */
    public ClusterAllocationBuilder businessInvoker(Function<List<TradeDataEntity>,Boolean> businessInvoker){
        this.businessInvoker = businessInvoker;
        return this;
    }

    /**
     * 业务代码函数
     * @param msgPollInvoker
     * @return
     */
    public ClusterAllocationBuilder msgPollInvoker(BiFunction<Map<String,Object> ,IClusterConsumerThreadService,Boolean> msgPollInvoker){
        this.msgPollInvoker = msgPollInvoker;
        return this;
    }

    /**
     * 每一批次执行的数量
     * @param batchSize
     * @return
     */
    public ClusterAllocationBuilder batchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    /**
     * 业务执行线程池
     * @param businessHandleExecutor
     * @return
     */
    public ClusterAllocationBuilder businessHandleExecutor(ExecutorService businessHandleExecutor) {

        this.businessHandleExecutor = businessHandleExecutor;
        return this;
    }

    /**
     * 业务执行线程池
     * @param msgPollExecutor
     * @return
     */
    public ClusterAllocationBuilder msgPollExecutor(ExecutorService msgPollExecutor) {

        this.msgPollExecutor = msgPollExecutor;
        return this;
    }
}
