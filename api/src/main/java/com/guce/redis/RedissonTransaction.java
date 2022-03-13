package com.guce.redis;

import org.redisson.api.BatchResult;
import org.redisson.api.RBatch;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

/**
 * @Author chengen.gce
 * @DATE 2021/11/18 11:10 下午
 */
public class RedissonTransaction {

    public static void main(String[] args) {
        RedissonClient redissonClient = null;
        assert redissonClient != null;
        RBatch batch = redissonClient.createBatch();
        batch.getBucket("name").setAsync("value");
        batch.getTopic("topic").publishAsync(new Object());
        BatchResult result = batch.execute();
        result.getResponses();


        RTopic topic = redissonClient.getTopic("topicName");
        topic.addListener(String.class, (channel, msg) -> {

        });

    }
}
