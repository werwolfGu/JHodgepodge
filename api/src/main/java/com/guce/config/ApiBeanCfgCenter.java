package com.guce.config;

import com.guce.AppDemo;
import com.guce.aop.MyAnnTestAnalysis;
import com.guce.example.ExampleDemo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiBeanCfgCenter {

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



    /*@Bean(value = "asyncCacheThreadFactory",initMethod = "init",destroyMethod = "destory")
    @ConditionalOnMissingBean
    public AsyncCacheThreadFactory asyncCacheThreadFactory(){
        AsyncCacheThreadFactory asyncCacheThreadFactory = new AsyncCacheThreadFactory();
        asyncCacheThreadFactory.setCoreThread(coreThreadNum);
        asyncCacheThreadFactory.setMaxThread(maxThread);
        asyncCacheThreadFactory.setKeepAlive(keepAlive);

        return asyncCacheThreadFactory;
    }*/

}
