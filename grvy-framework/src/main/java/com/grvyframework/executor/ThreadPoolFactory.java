package com.grvyframework.executor;

import com.grvyframework.config.ExecutorConfig;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chengen.gu
 * @date 2020-01-20 16:27
 * @description
 */
public class ThreadPoolFactory {

    public static ThreadPoolExecutor getThreadPoolExecutor(ExecutorConfig config){

        BlockingQueue queue = new LinkedBlockingQueue(config.getQueueSize());

        ThreadPoolExecutor executor = new ThreadPoolExecutor(config.getCorePoolSize(),config.getMaximumPoolSize()
                ,config.getKeepAliveTime(), TimeUnit.MINUTES,queue);
        return executor;
    }
}
