package com.guce.caffeine;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by chengen.gu on 2018/10/30.
 */
public class CaffeineDemo {

    private Logger logger = LoggerFactory.getLogger(CaffeineDemo.class);

    private LoadingCache<String, String> cache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .refreshAfterWrite(1,TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                @Nullable
                @Override
                public String load(@Nonnull String k1) throws Exception {
                    Thread.sleep(100);
                    return k1;
                }
            });

    public String demo(String key) throws ExecutionException, InterruptedException {
        String future = cache.get(key);
        return future;
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CaffeineDemo demo = new CaffeineDemo();
        for(int i = 0 ; i < 100 ; i++ ){

            Thread.sleep(i %20);

            String key = "i->" + (i %10);

            System.out.println(demo.demo(key));
        }
    }
}
