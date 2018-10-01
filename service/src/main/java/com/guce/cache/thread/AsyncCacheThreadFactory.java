package com.guce.cache.thread;


import com.google.common.cache.CacheLoader;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncCacheThreadFactory {

    @Value("${guava.core.thread.num:4}")
    private int coreThread;

    @Value("${guava.max.thread.num:16}")
    private int maxThread;

    @Value("${guava.keepalive.time:1800000}")
    private long keepAlive;

    @Value(("${guava.queue.capacity:0}"))
    private int capacity;

    private ThreadPoolExecutor executor;

    @PostConstruct
    public void init(){

        int capacity = Integer.MAX_VALUE;
        if( this.capacity != 0){
            capacity = this.capacity;
        }
        BlockingQueue blockingQueue = new LinkedBlockingQueue(capacity);

        executor = new ThreadPoolExecutor(coreThread, maxThread, keepAlive, TimeUnit.SECONDS, blockingQueue, new ThreadFactory() {
            private final AtomicInteger poolNumber = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {

                String threadName = "guava-thread-pool-" + poolNumber.incrementAndGet();
                Thread th = new Thread(r,threadName);
                return th;
            }
        });
    }

    public <K,V> CacheLoader<K,V> newAsyncCacheBuilder(CacheLoader<K,V> cacheLoader){
        return CacheLoader.asyncReloading(cacheLoader,executor);
    }

    @PreDestroy
    public void destory(){

        if(executor != null){
            executor.shutdown();
        }
    }

    public int getCoreThread() {
        return coreThread;
    }

    public void setCoreThread(int coreThread) {
        this.coreThread = coreThread;
    }

    public int getMaxThread() {
        return maxThread;
    }

    public void setMaxThread(int maxThread) {
        this.maxThread = maxThread;
    }

    public long getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(long keepAlive) {
        this.keepAlive = keepAlive;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
