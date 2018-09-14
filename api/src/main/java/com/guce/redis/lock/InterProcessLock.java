package com.guce.redis.lock;

import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

public interface InterProcessLock {

    /**
     * redis加锁
     * @param jedisCommands
     * @param key
     * @return
     */
    public boolean lock(Jedis jedisCommands, String key) throws InterruptedException;

    public boolean tryLock(Jedis jedisCommands , String key, long time, TimeUnit timeUnit) throws InterruptedException;

    /**
     * redis 解锁
     * @param jedisCommands
     * @param key
     * @return
     */
    public boolean unlock(Jedis jedisCommands, String key);
}
