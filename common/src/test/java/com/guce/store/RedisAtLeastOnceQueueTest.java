package com.guce.store;

import com.guce.queue.RedisAtLeastOnceQueue;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @Author chengen.gce
 * @DATE 2021/7/31 8:43 下午
 */
class RedisAtLeastOnceQueueTest {

    @Test
    void delayHandle() {
        System.out.println(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
        double d = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        System.out.println(d);
        RedisAtLeastOnceQueue delayQueue = new RedisAtLeastOnceQueue();

        delayQueue.delayHandle((key) -> {
            System.out.println("do something ...." + delayQueue.in.getAndIncrement());
            return null;
        }, null, "delayQueueName", 4);
    }
}