package com.grvyframework.manager;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.grvyframework.grvy.engine.GrvyClassLoader;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GroovyDynamicsScriptManager<V> {


    private final static Logger logger = LoggerFactory.getLogger(GroovyDynamicsScriptManager.class);

    private static volatile GroovyDynamicsScriptManager grvy ;

    @Getter
    private LoadingCache<String, V> grvyClassCache;

    @Getter
    private Map<String,String> grvyScriptMapper = new ConcurrentHashMap<>();

    private GroovyDynamicsScriptManager(){

        grvyClassCache = CacheBuilder.newBuilder().maximumSize(1000).build(new CacheLoader<String, V>() {

            @Override
            public V load(String s) throws Exception {

                return loadGrvyClassInstance(s);
            }
        });
    }

    private V loadGrvyClassInstance(String key) throws IllegalAccessException, IOException, InstantiationException {

        String grvyScriptPaht = grvyScriptMapper.get(key);
        logger.warn("load class key:{} ; classpath:{}" ,key,grvyScriptPaht);
        System.out.println("load class key:"+key + "; classpath:"  + grvyScriptPaht);
        V instance = GrvyClassLoader.loaderInstance(grvyScriptPaht);
        return instance;
    }


}
