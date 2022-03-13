package com.guce.redis.lock;

import org.redisson.Redisson;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RLock;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RedissonClient;

/**
 * @Author chengen.gce
 * @DATE 2020/10/1 1:07 下午
 */
public class RedissonLock {

    public static void main(String[] args) {
        RedissonClient client = Redisson.create();
        RLock lock = client.getLock("lock");
        lock.lock();

        lock.unlock();
        RRateLimiter rRateLimiter = client.getRateLimiter("");
        rRateLimiter.acquire();
        RLocalCachedMap<String, String> localCachedMap =
                client.getLocalCachedMap("test", LocalCachedMapOptions.defaults());

    }
}
