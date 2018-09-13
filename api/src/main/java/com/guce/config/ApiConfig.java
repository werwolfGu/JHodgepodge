package com.guce.config;

import com.guce.example.ExampleDemo;
import com.guce.AppDemo;
import com.guce.aop.MyAnnTestAnalysis;
import com.guce.thread.AsyncCacheThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ApiConfig {

    @Value("${guava.core.thread.num:4}")
    private int coreThreadNum;

    @Value("${guava.max.thread.num:16}")
    private int maxThread;

    @Value("${guava.keepalive.time:1800000}")
    private long keepAlive;

    @Value(("${guava.queue.capacity:0}"))
    private int capacity;
    private ThreadPoolExecutor executor;

    @Bean("exampleDemo")
    public ExampleDemo exampleDemo(){
        return new ExampleDemo();
    }

    @Bean("appDemo")
    public AppDemo appDemo(){
        return new AppDemo();
    }

    @Bean("myAnnTestAnalysis")
    public MyAnnTestAnalysis myAnnTestAnalysis(){
        return new MyAnnTestAnalysis();
    }

    @Bean(value = "asyncCacheThreadFactory",initMethod = "init",destroyMethod = "destory")
    public AsyncCacheThreadFactory asyncCacheThreadFactory(){
        AsyncCacheThreadFactory asyncCacheThreadFactory = new AsyncCacheThreadFactory();
        asyncCacheThreadFactory.setCoreThread(coreThreadNum);
        asyncCacheThreadFactory.setMaxThread(maxThread);
        asyncCacheThreadFactory.setKeepAlive(keepAlive);

        return asyncCacheThreadFactory;
    }

}
