package com.guce.groovy;


import lombok.Getter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author guchengen495
 * @date 2020-01-19 15:53
 * @description
 */
public class GrvyEngineThreadPool {

    /**
     * script engine 线程池
     */
    @Getter
    public static ThreadPoolExecutor poolExecutor ;

    static {
        int cpuCore = Runtime.getRuntime().availableProcessors() + 2;
        poolExecutor = new ThreadPoolExecutor(cpuCore, cpuCore * 2 ,10
                ,TimeUnit.MINUTES,new LinkedBlockingQueue<>(1000));
    }
}
