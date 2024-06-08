package com.guce.allocation.manager;

import com.guce.allocation.TradeDataEntity;
import com.guce.loadbalance.LoadBalance;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Author chengen.gce
 * @DATE 2022/5/13 14:19
 */
@Slf4j
public class ClusterThreadDispatcherManager<R> implements IClusterConsumerThreadService {

    @Setter
    private int threadConcurrentNumber = 1;
    @Setter
    private Function<List<TradeDataEntity>,R> businessInvoker;
    @Setter
    private Supplier<Boolean> interruptedLoopInvoker;

    @Setter
    private int batchSize = 1;
    @Setter
    private String businessName;

    private AtomicBoolean producerEndFlag = new AtomicBoolean(false);
    @Setter
    private ClusterDispatchManager clusterAllocationManager;
    @Setter
    ExecutorService businessConsumeExecutor ;

    @Setter
    ExecutorService msgPollExecutor ;

    @Setter
    private LoadBalance loadBalance;

    public ProducerRateWaitPolicy producerRateWaitPolicy ;

    private int DEFAULT_QUEUE_CAPACITY = 100000;
    private long DEFAULT_PRODUCE_QUEUE_WAIT_TIME = 3;

    private Map<String,WorkThread> workThreadrouteTableMap = new ConcurrentHashMap<>();
    private List<String> threadRouteInvokers = new ArrayList<>(16);

    public ClusterThreadDispatcherManager(String businessName) {
        this.businessName = businessName;
        this.producerRateWaitPolicy = new CapacityControlWaitPolicy(DEFAULT_QUEUE_CAPACITY,businessName,DEFAULT_PRODUCE_QUEUE_WAIT_TIME);
    }

    /**
     * 将待处理数据分配到每个业务线程队列中
     * @param dataEntityList
     */
    public void selectDispatchExecuteThread(List<TradeDataEntity> dataEntityList){

        List<String> threadList = threadRouteInvokers;
        dataEntityList.parallelStream().forEach( dataEntity -> {
            ////选择要执行的线程
            String selectedThread = loadBalance.select(threadList,dataEntity.getThreadIdentifierCode());
            WorkThread thread = workThreadrouteTableMap.get(selectedThread);
            AllocationQueueManager.getBusinessQueueLenMap()
                    .get(businessName).add(1);
            AllocationQueueManager.getBusinessQueueSizeMap()
                    .get(businessName).add(1);

            BlockingQueue queue = thread.getThreadWorkQueue();
            queue.add(dataEntity);
        });
        producerRateWaitPolicy.rateWait();

    }

    /**
     * 消息分配到不同的机器，同时分配到不同的线程
     * @param list
     */
    @Override
    public void allocationToConsumerThreadQueue (List<TradeDataEntity> list) {

        ///本机执行交易数据
        list = clusterAllocationManager.selectDispatchJVM(list);
        if (CollectionUtils.isEmpty(list)){
            return ;
        }
        ////分配到个线程执行的数据
        this.selectDispatchExecuteThread(list);
    }

    /**
     * 启动数据拉去
     */
    @Override
    public CompletableFuture startupBusinessMsgPoll(Consumer<Map<String,Object>> mapConsumer) {

        return CompletableFuture.runAsync( () -> {
            mapConsumer.accept(null);
        },msgPollExecutor).whenComplete( (Void,e) -> {
            log.info("生成的数据数量：" + AllocationQueueManager.getBusinessQueueLenMap().get(businessName).longValue());
            finished();
        } );

    }

    /**
     * 启动数据拉去
     */
    @Override
    public CompletableFuture startupBusinessMsgPoll(final Map<String,Object> param ,BiFunction<Map<String,Object>,IClusterConsumerThreadService,Boolean> function) {

        return CompletableFuture.runAsync( () -> {
            function.apply(param,this);
        },msgPollExecutor).whenComplete( (Void,e) -> {
            log.info("生成的数据数量：" + AllocationQueueManager.getBusinessQueueLenMap().get(businessName).longValue());
            finished();
        } );

    }

    /**
     * 启动业务消费线程
     * @return
     */
    @Override
    public CompletableFuture startupBusinessHandleThread () {

        List<CompletableFuture> futureList = new ArrayList<>(threadConcurrentNumber);
        for (int i = 0 ; i < threadConcurrentNumber ; i++ ){
            ////创建工作线程
            WorkThread<R> tradeHandleThread =
                    new WorkThread(producerEndFlag, batchSize, businessInvoker,interruptedLoopInvoker);
            String threadIdentifierCode = String.valueOf(System.identityHashCode(tradeHandleThread));
            ///分配线程队列
            BlockingQueue<TradeDataEntity> threadWorkQueue = AllocationQueueManager.allocationBusinessThreadQueue(businessName,threadIdentifierCode,1000000);

            tradeHandleThread.setThreadIdentifierCode(threadIdentifierCode);
            tradeHandleThread.setThreadWorkQueue(threadWorkQueue);
            workThreadrouteTableMap.put(threadIdentifierCode,tradeHandleThread);
            threadRouteInvokers.add(threadIdentifierCode);
            CompletableFuture future = CompletableFuture
                    .runAsync(tradeHandleThread ,businessConsumeExecutor)
                    .whenComplete( (Void ,e) -> {
                        log.info(Thread.currentThread().getName() + " end ,clear thread route info");
                        workThreadrouteTableMap.remove(threadIdentifierCode);
                        threadRouteInvokers.remove(threadIdentifierCode);
                    });

            futureList.add(future);
        }
        if (CollectionUtils.isNotEmpty(futureList)) {

            CompletableFuture[] futures = futureList.toArray(new CompletableFuture[0]);
            CompletableFuture allFuture = CompletableFuture.allOf(futures)
                    .whenComplete( (Void, e) -> {

                        long dealSize = AllocationQueueManager.getBusinessQueueLenMap().get(businessName).longValue();
                        long unDealSize = AllocationQueueManager.getBusinessQueueSizeMap().get(businessName).longValue();
                        log.info("business {} end , and deal data size : {} ; undeal data size: {}"
                                , businessName,dealSize,unDealSize);
                        AllocationQueueManager.clear(businessName);
            });
            return allFuture;
        }
        return null;
    }

    public void clearThread(String threadIdentifierCode){
        ////从路由表中去除
        workThreadrouteTableMap.remove(threadIdentifierCode);
    }

    public void finished(){
        producerEndFlag.set(true);
    }

    /**
     * 业务执行线程
     * @param <R>
     */
    private class WorkThread<R> implements Runnable{

        private final AtomicBoolean producerEndFlag ;
        private final int batchSize;
        @Setter
        private String threadIdentifierCode;

        @Setter
        @Getter
        private BlockingQueue<TradeDataEntity> threadWorkQueue;

        private Function<List<TradeDataEntity>,R> invoke;

        ////线程中断调用
        private Supplier<Boolean> interruptedLoopInvoker;
        private LongAdder count = new LongAdder();

        public WorkThread( AtomicBoolean isEndFlag , int batchSize, Function<List<TradeDataEntity>,R> function ,Supplier<Boolean> interruptedLoopInvoker) {
            this.producerEndFlag = isEndFlag;
            this.batchSize = batchSize;
            this.invoke = function;
            this.interruptedLoopInvoker = interruptedLoopInvoker;
        }

        @Override
        public void run() {

            List<TradeDataEntity> list = new ArrayList<>(100);
            BlockingQueue<TradeDataEntity> queue = threadWorkQueue;
            while (true){

                boolean interrupted = interruptedLoopInvoker.get();
                if (interrupted){
                    log.warn(Thread.currentThread().getName() + " 线程终止！");
                    break;
                }
                try {

                    if (producerEndFlag.get() && queue.isEmpty() && CollectionUtils.isEmpty(list)){
                        break;
                    }
                    TradeDataEntity data = queue.poll(1, TimeUnit.SECONDS);
                    if (data == null) {
                        Thread.yield();
                    }
                    if (data != null) {
                        AllocationQueueManager.getBusinessQueueSizeMap()
                                .get(data.getBusinessName()).decrement();
                        list.add(data);
                    }

                    if (list.size() >= batchSize) {
                        ///handle
                        invoke.apply(list);
                        count.add(list.size());
                        list.clear();
                    }
                    if (producerEndFlag.get() && queue.isEmpty()) {
                        ////handle
                        invoke.apply(list);
                        count.add(list.size());
                        list.clear();
                    }
                } catch (InterruptedException e) {
                    log.error("业务线程执行异常",e);
                }

            }
            clearThread(threadIdentifierCode);
            log.info(Thread.currentThread().getName()+ "-" + threadIdentifierCode + " end ; deal data size: " + count.longValue() + " queue size : " + queue.size());
        }
    }

    public static class CapacityControlWaitPolicy  extends ProducerRateWaitPolicy {

        private int capacity ;
        private String businessName;
        private long sleepTime;
        public CapacityControlWaitPolicy (int capacity , String businessName,long sleepTime){
            this.capacity = capacity;
            this.businessName = businessName;
            this.sleepTime = sleepTime;
        }
        @Override
        public void rateWait() {
            long unDealSize = AllocationQueueManager.getBusinessQueueLenMap()
                    .get(businessName).longValue();
            while (unDealSize > capacity ){
                try {
                    Thread.sleep(sleepTime);
                    unDealSize = AllocationQueueManager.getBusinessQueueSizeMap()
                            .get(businessName).longValue();
                } catch (InterruptedException e) {
                    log.error("sleep exception: ",e);
                }
            }
        }
    }
    public static abstract class ProducerRateWaitPolicy {

        public abstract void rateWait () ;
    }
}
