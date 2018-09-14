package com.guce.redis.lock;

import com.guce.redis.lock.impl.InterProcessRedisMutexLock;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.IOException;

public class LockTest {

    @Test
    public void Test() {

        for(int i = 0 ; i < 10 ;i++ ){

            Jedis jedis = new Jedis("redis.local",6379);
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {


                    InterProcessLock lock = new InterProcessRedisMutexLock();
                    try {
                        lock.lock(jedis,"lock");
                        System.out.println("lock-name:" + Thread.currentThread().getName());
                        Thread.sleep(1000);
                        try{
                            lock.lock(jedis,"lock");
                            System.out.println("lock-name:" + Thread.currentThread().getName() + " 再次进入");
                            Thread.sleep(500);
                        }finally {
                            lock.unlock(jedis,"lock");
                        }
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }finally {
                        System.out.println("unlock-name:" + Thread.currentThread().getName());
                        lock.unlock(jedis,"lock");
                    }
                }
            });

            th.setName("thread-" + i);
            th.start();
        }
        try {
            System.out.println("enter :");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
