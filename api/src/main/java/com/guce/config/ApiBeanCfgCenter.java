package com.guce.config;

import com.guce.AppDemo;
import com.guce.aop.MyAnnTestAnalysis;
import com.guce.example.ExampleDemo;
import com.guce.thread.AsyncCacheThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.guce")
public class ApiBeanCfgCenter {

    @Value("${guava.core.thread.num:4}")
    private int coreThread;

    @Value("${guava.max.thread.num:16}")
    private int maxThread;

    @Value("${guava.keepalive.time:1800000}")
    private long keepAlive;

    @Value(("${guava.queue.capacity:0}"))
    private int capacity;

    @Bean("exampleDemo")
    public ExampleDemo exampleDemo(){
        return new ExampleDemo();
    }

    @Bean
    public AppDemo appDemo(){
        return new AppDemo();
    }

    @Bean("myAnnTestAnalysis")
    public MyAnnTestAnalysis myAnnTestAnalysis(){
        return new MyAnnTestAnalysis();
    }



    @Bean(value = "asyncCacheThreadFactory",initMethod = "init",destroyMethod = "destory")
    @ConditionalOnMissingBean
    public AsyncCacheThreadFactory asyncCacheThreadFactory(){
        AsyncCacheThreadFactory asyncCacheThreadFactory = new AsyncCacheThreadFactory();
        asyncCacheThreadFactory.setCoreThread(coreThread);
        asyncCacheThreadFactory.setMaxThread(maxThread);
        asyncCacheThreadFactory.setKeepAlive(keepAlive);
        asyncCacheThreadFactory.setCapacity(capacity);

        return asyncCacheThreadFactory;
    }

}
