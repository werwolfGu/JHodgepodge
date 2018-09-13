package com.guce.cache.loader;

import com.google.common.cache.CacheLoader;
import org.springframework.stereotype.Component;

@Component
public class SampleCacheLoader extends CacheLoader<String,String>{


    @Override
    public String load(String key) throws Exception {

        return "hello guava";
    }

}
