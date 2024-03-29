package com.grvyframework.grvy.engine;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import groovy.lang.GroovyClassLoader;
import org.apache.commons.collections.CollectionUtils;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.jsr223.GroovyCompiledScript;
import org.codehaus.groovy.jsr223.GroovyScriptEngineImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * grvy类加载每次都要加载一次的，可能会导致metaspace oom  ,现在将script脚本类缓存起来 ；就无需每次都要重新生成class了
 * @author chengen.gu
 * @date 2020-01-20 15:52
 * @description
 */
@Component("grvyScriptEngineClient")
public class GrvyScriptEngineClient {

    private static Logger logger = LoggerFactory.getLogger(GrvyScriptEngineClient.class);

    @Resource(name = "grvyScriptEngine")
    private GrvyScriptEngine grvyScriptEngine;

    private GroovyClassLoader loader;

    private LoadingCache<String,Class<?>> grvyClassLoaderCache =
            CacheBuilder.newBuilder().maximumSize(1000)
                    ///访问30分钟后无再访问 失效
                    //.expireAfterAccess(30, TimeUnit.MINUTES)
                    .removalListener( notification ->
                            logger.warn("######guava cache grvy script:{}",notification.getKey()))
                    .build(new CacheLoader<String, Class<?>>() {
                        @Override
                        public Class<?> load(String key) {

                                return loaderGrvyClazz(key);
                        }
                    });

    private LoadingCache<String,GroovyCompiledScript> grvyCompileScriptCache =
            CacheBuilder.newBuilder().maximumSize(1000)
            ///访问30分钟后无再访问 失效
            //.expireAfterAccess(30, TimeUnit.MINUTES)
            .removalListener( notification ->
                    logger.warn("######guava cache grvy script:{}",notification.getKey()))
            .build(new CacheLoader<String, GroovyCompiledScript>() {
                @Override
                public GroovyCompiledScript load(String key) {

                    return loadGrvyCompiledScript(key);
                }
            });

    public GrvyScriptEngineClient() {
        this(AccessController.doPrivileged(new PrivilegedAction<GroovyClassLoader>() {
            @Override
            public GroovyClassLoader run() {
                return new GroovyClassLoader(getParentLoader(), new CompilerConfiguration(CompilerConfiguration.DEFAULT));
            }
        }));
    }
    public GrvyScriptEngineClient(GroovyClassLoader classLoader) {
        if (classLoader == null) {
            throw new IllegalArgumentException("GroovyClassLoader is null");
        }
        this.loader = classLoader;
    }

    private Class<?> loaderGrvyClazz(String grvyScript){

        long start = System.currentTimeMillis();
        Class<?> clazz = loader.parseClass(grvyScript);
        if (logger.isInfoEnabled()){
            logger.info("====loader Grvy clazz -> {{}} ; cost time:{}" ,grvyScript,System.currentTimeMillis() - start);
        }
        return  clazz;

    }

    private GroovyCompiledScript loadGrvyCompiledScript(String grvyScript){
        long start = System.currentTimeMillis();
        Class<?> clazz = loader.parseClass(grvyScript);
        if (logger.isInfoEnabled()){
            logger.info("====loader Grvy clazz -> {{}} ; cost time:{}" ,grvyScript,System.currentTimeMillis() - start);
        }
        return  new GroovyCompiledScript((GroovyScriptEngineImpl)grvyScriptEngine.getScriptEngine(),clazz);

    }

    public <T> T eval(String script, ScriptContext scripCtx) throws ExecutionException, ScriptException {

        GroovyCompiledScript compiledScript = grvyCompileScriptCache.get(script);
        Object result;
        //synchronized (compiledScript) {
            result = compiledScript.eval(scripCtx);
        //}
        return (T) result;
    }

    private static ClassLoader getParentLoader() {
        // check whether thread context loader can "see" Groovy Script class
        ClassLoader ctxtLoader = Thread.currentThread().getContextClassLoader();
        try {
            Class<?> c = ctxtLoader.loadClass(GrvyScriptEngineClient.class.getName());
            if (c == GrvyScriptEngineClient.class) {
                return ctxtLoader;
            }
        } catch (ClassNotFoundException cnfe) {
            /* ignore */
        }
        // exception was thrown or we get wrong class
        return GrvyScriptEngineClient.class.getClassLoader();
    }

    public void flushLoadingCache(String key){

        flushLoadingCache(Arrays.asList(key));
    }

    public void flushLoadingCache(List<String> keyList){

        logger.warn("####guava cache invalid grvy script info:{}" ,keyList);
        if (CollectionUtils.isNotEmpty(keyList)){
            grvyCompileScriptCache.invalidateAll(keyList);
            grvyClassLoaderCache.invalidateAll(keyList);

        }
    }
}
