package com.guce.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class SimpleExample {

    private LoadingCache<String,String> loadingCache = CacheBuilder

            .newBuilder().maximumSize(1000)
            .refreshAfterWrite(30000, TimeUnit.SECONDS)
            .removalListener(new RemovalListener<String, String>() {
                @Override
                public void onRemoval(RemovalNotification<String, String> notification) {
                    System.out.println("guava-delete cause:" + notification.getCause()
                            + "-> key:"+ notification.getKey()
                            + "-> value:" + notification.getValue());
                }
            }).build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    return "hello guava";
                }
            });

    public String getValue(String key) throws ExecutionException {

        return loadingCache.get(key);
    }
    public static void main(String[] args) {
        SimpleExample simpleExample = new SimpleExample();
        try {
            String value = simpleExample.getValue("key");
            System.out.println("value: " + value);
        } catch (ExecutionException e) {

        }
    }
}
