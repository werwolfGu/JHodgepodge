package com.guce.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.CompatibleWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public abstract class AbstractGuavaCache<K,V>{

    private static Logger logger = LoggerFactory.getLogger(AbstractGuavaCache.class);
    private LoadingCache<K,V> delegate;

    public AbstractGuavaCache(CacheLoader<K,V> cacheLoader,long maxSize, long refreshTime, TimeUnit unit){

        delegate = CacheBuilder
                .newBuilder()
                .refreshAfterWrite(refreshTime, unit)
                .removalListener(new RemovalListener<Object, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<Object, Object> notification) {
                        if(logger.isInfoEnabled()){
                            logger.info("删除 cause：{} -> key:{} -> value{}"
                                    ,notification.getCause(),notification.getKey(),notification.getValue());
                        }
                    }
                })
//                .expireAfterWrite(maxSize,unit)
                .maximumSize(maxSize)
                .build(cacheLoader);
    }

    public V get(K key) throws ExecutionException {

        return delegate.get(key);
    }

    public V getIfPresent(@CompatibleWith("K") Object key){

        return delegate.getIfPresent(key);
    }

    public ImmutableMap<K, V> getAllPresent(Iterable<?> iterable){

        return delegate.getAllPresent(iterable);
    }

    public void put(K key, V value){
        delegate.put(key,value);
    }

    public void putAll(Map<? extends K, ? extends V> map){
        delegate.putAll(map);
    }


    public void invalidateAll(Iterable<?> iterable){
        delegate.invalidateAll(iterable);
    }

    public void invalidateAll(){
        delegate.invalidateAll();
    }

    public ConcurrentMap<K, V> asMap() {
        return delegate.asMap();
    }

}
