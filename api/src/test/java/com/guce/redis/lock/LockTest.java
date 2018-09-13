package com.guce.redis.lock;

import com.guce.redis.lock.impl.InterProcessRedisMutexLock;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class LockTest {

    @Test
    public void redisLockTest(){

        Jedis jedis = new Jedis("127.0.0.1",6379);

        InterProcessLock lock = new InterProcessRedisMutexLock();
        try {
            lock.lock(jedis,"lock");
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }finally {
            lock.unlock(jedis,"lock");
        }
    }
}
