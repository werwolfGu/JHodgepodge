package com.guce.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @Author chengen.gce
 * @DATE 2021/7/28 9:19 下午
 */
@Slf4j
public class LocalSwitchManager<K> {

    protected Cache<K,Object> localCahe = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .refreshAfterWrite(3,TimeUnit.SECONDS)
            .maximumSize(10000)
            .build(this::invoke);

    public <V> V getlocalSwitchValue(K key , Function<K,V> function){

        return (V) localCahe.get(key,function);
    }

    public <V> V getlocalSwitchValue(K key ){

        return (V) localCahe.getIfPresent(key);
    }



    public  <V> V invoke(K key) {

        return null;
    }


    public static void main(String[] args) {
        //
        System.out.println(12 % 18);
    }

}
