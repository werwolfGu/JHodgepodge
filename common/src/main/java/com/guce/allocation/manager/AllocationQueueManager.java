package com.guce.allocation.manager;

import com.guce.allocation.TradeDataEntity;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.LongAdder;

/**
 * 用于管理线程队列，每个线程都有一个对应的队列消费消息
 * @Author chengen.gce
 * @DATE 2022/5/12 14:59
 */
public class AllocationQueueManager  {

    /**
     * 每一个业务 每个线程对应一个队列
     */
    @Getter
    private static final Map<String, Map<String,BlockingQueue<TradeDataEntity>>> businessThreadsQueueMap = new ConcurrentHashMap<>(64);

    @Getter
    private static final Map<String, LongAdder> businessQueueSizeMap = new ConcurrentHashMap<>(64);
    @Getter
    private static final Map<String, LongAdder> businessQueueLenMap = new ConcurrentHashMap<>(64);

    public static BlockingQueue<TradeDataEntity> allocationBusinessThreadQueue (String bussinessName,String threadIdentifierCode , int capacity ){
        businessQueueSizeMap.computeIfAbsent( bussinessName , key -> new LongAdder());
        businessQueueLenMap.computeIfAbsent( bussinessName , key -> new LongAdder());

        return businessThreadsQueueMap
                .computeIfAbsent(bussinessName , key ->  new ConcurrentHashMap<>(64))
                .computeIfAbsent(threadIdentifierCode , key -> new LinkedBlockingQueue(capacity));
    }

    /**
     * 清空业务队列
     * @param businessCode
     */
    public static void clear(String businessCode) {
        businessThreadsQueueMap.remove(businessCode);
    }
}
