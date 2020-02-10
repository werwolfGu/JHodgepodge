package com.grvyframework.pool;

import com.grvyframework.config.ExecutorConfig;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chengen.gu
 * @date 2020-01-20 16:27
 * @description
 */
public class ThreadPoolFactory {

    private static AtomicInteger threadNumber = new AtomicInteger(1);

    private final static String GRVY_THREAD_NAME_PREFIX = "grvy-t-pool-";

    public static ThreadPoolExecutor getThreadPoolExecutor(ExecutorConfig config){


        int capacity = Optional.ofNullable(config)
                .map(ExecutorConfig::getQueueSize)
                .map(size -> size <= 0 ? null : size )
                .orElse(Integer.MAX_VALUE);

        int coreCpu = Optional.ofNullable(config)
                .map(ExecutorConfig::getCorePoolSize)
                .map(size -> size <= 0 ? null : size )
                .orElseGet( () -> Runtime.getRuntime().availableProcessors() + 1);

        int maximumPoolSize = Optional.ofNullable(config)
                .map(ExecutorConfig::getMaximumPoolSize)
                .map(size -> size <= 0 ? null : size )
                .orElseGet( () -> coreCpu * 2 );

        long keepAliveTime = Optional.ofNullable(config)
                .map(ExecutorConfig::getKeepAliveTime)
                .map(size -> size <= 0 ? null : size )
                .orElse(3L);

        String threadNamePrefix = Optional.ofNullable(config)
                .map(ExecutorConfig::getThreadName)
                .orElse(GRVY_THREAD_NAME_PREFIX);

        BlockingQueue queue = new LinkedBlockingQueue(capacity);

        return new ThreadPoolExecutor(coreCpu, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES, queue
                , r -> {

            String name = threadNamePrefix + threadNumber.getAndIncrement();
            Thread t = new Thread(r, name);
            if (t.isDaemon()){
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY){
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        });
    }
}
