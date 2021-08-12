package com.guce.store;

import com.guce.queue.RedisDelayQueue;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @Author chengen.gce
 * @DATE 2021/7/31 8:43 下午
 */
class RedisDelayQueueTest {

    @Test
    void delayHandle() {
        System.out.println(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
        double d = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        System.out.println(d);
        RedisDelayQueue delayQueue = new RedisDelayQueue();
        Runnable runnable = () -> {
            long time = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
            System.out.println(Thread.currentThread().getName() + " 5555555 rate1 :" + time);
        };

        delayQueue.delayHandle(5, runnable);

        Runnable runnable1 = () -> {
            long time = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
            System.out.println(Thread.currentThread().getName() + " 33333333 rate2 :" + time);
        };

        delayQueue.delayHandle(3, runnable1);
    }
}