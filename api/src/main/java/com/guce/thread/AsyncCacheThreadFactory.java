package com.guce.thread;


import com.google.common.cache.CacheLoader;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class AsyncCacheThreadFactory {

    private int coreThread;

    private int maxThread;

    private long keepAlive;

    private int capacity = 0 ;

    private ThreadPoolExecutor executor;

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
