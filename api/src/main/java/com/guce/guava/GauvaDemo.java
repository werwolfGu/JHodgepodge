package com.guce.guava;

import com.google.common.base.Splitter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by chengen.gu on 2019/9/29.
 */
public class GauvaDemo {

    private LoadingCache<String,String> loadingCache = CacheBuilder

            .newBuilder().maximumSize(100000)
            //.refreshAfterWrite(30000, TimeUnit.SECONDS)
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
                    return "hello guava" + key;
                }
            });

    public String getValue(String key) throws ExecutionException {

        return loadingCache.get(key);
    }
    public static void main(String[] args) {
        Splitter.on(",").split("a,b,c,d").forEach(s -> System.out.println(s));

        GauvaDemo simpleExample = new GauvaDemo();
        try {
            for(int i = 0 ; i < 10 ; i++){
                int idx = i;
                if (i == 3){
                    idx = 0;
                }
                if ( i == 4){
                    idx = 1;
                }
                if ( i == 5){
                    idx = 0;
                }
                String value = simpleExample.getValue("key" + idx);

            }

            int idx = 0;
            while (idx <= 1000000) {
                simpleExample.getValue("key" + idx);
                idx++;

            }
            System.in.read();
        } catch (ExecutionException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
