package com.guce.groovy.pool;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author guchengen495
 * @date 2020-01-20 09:47
 * @description
 */
public class GrvyEngineThreadPool {

    @Getter
    private static ThreadPoolExecutor poolExecutor ;

    private final static AtomicInteger threadNumber = new AtomicInteger(1);

    static {

        int cupCore = Runtime.getRuntime().availableProcessors() + 4;
        LinkedBlockingQueue workQueue = new LinkedBlockingQueue(10000);
        poolExecutor = new ThreadPoolExecutor(cupCore, cupCore * 2, 3, TimeUnit.MINUTES, workQueue
                , r -> {
                    Thread t = new Thread();
                    t.setName("grvy->" + threadNumber.getAndIncrement());
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
