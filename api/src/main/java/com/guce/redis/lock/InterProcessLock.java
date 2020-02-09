package com.guce.redis.lock;

import redis.clients.jedis.commands.JedisCommands;

import java.util.concurrent.TimeUnit;

public interface InterProcessLock {

    /**
     * redis加锁
     * @param jedisCommands
     * @param key
     * @return
     */
    public boolean lock(JedisCommands jedisCommands, String key) throws InterruptedException;

    public boolean tryLock(JedisCommands jedisCommands , String key, long time, TimeUnit timeUnit) throws InterruptedException;

    /**
     * redis 解锁
     * @param jedisCommands
     * @param key
     * @return
     */
    public boolean unlock(JedisCommands jedisCommands, String key);
}
