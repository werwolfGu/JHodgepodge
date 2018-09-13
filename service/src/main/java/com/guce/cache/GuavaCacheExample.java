package com.guce.cache;

import com.guce.cache.loader.SampleCacheLoader;
import com.guce.guava.AbstractGuavaCache;
import com.guce.thread.AsyncCacheThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component("guavaCacheExample")
public class GuavaCacheExample extends AbstractGuavaCache<String,String> {

    @Autowired
    public GuavaCacheExample(SampleCacheLoader cacheLoader,AsyncCacheThreadFactory asyncCacheThreadFactory) {

        super(asyncCacheThreadFactory.newAsyncCacheBuilder(cacheLoader),1000,180,TimeUnit.SECONDS);

    }



}
