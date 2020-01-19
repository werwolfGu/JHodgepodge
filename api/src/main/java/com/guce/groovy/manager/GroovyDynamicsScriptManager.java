package com.guce.groovy.manager;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.guce.groovy.GrvyScriptMapper;
import com.guce.groovy.IFoo;
import com.guce.groovy.engine.GrvyClassLoader;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class GroovyDynamicsScriptManager {


    private final static Logger logger = LoggerFactory.getLogger(GroovyDynamicsScriptManager.class);

    private static volatile GroovyDynamicsScriptManager grvy ;

    @Getter
    private LoadingCache<String, IFoo> grvyClassCache;

    public static GroovyDynamicsScriptManager getInstance(){
        if (grvy == null){
            synchronized (GroovyDynamicsScriptManager.class){
                if (grvy == null){
                    grvy = new GroovyDynamicsScriptManager();
                }
            }
        }
        return grvy;
    }

    private GroovyDynamicsScriptManager(){

        grvyClassCache = CacheBuilder.newBuilder().maximumSize(1000).build(new CacheLoader<String, IFoo>() {

            @Override
            public IFoo load(String s) throws Exception {
                return loadGrvyClassInstance(s);
            }
        });
    }

    public IFoo loadGrvyClassInstance(String key){

        IFoo iFoo = null;
        try {
            String scriptPath =GrvyScriptMapper.grvyScriptMap.get(key);
            iFoo = GrvyClassLoader.loadClass(scriptPath);
        } catch (IllegalAccessException | IOException | InstantiationException e) {
            logger.error("guava cache grovy dynamics class loader error!" ,e);
        }
        return iFoo;
    }

    public static void main(String[] args) throws ExecutionException {

        IFoo iFoo = GroovyDynamicsScriptManager.getInstance().getGrvyClassCache().get("普卡");
        iFoo.print();
    }

}
