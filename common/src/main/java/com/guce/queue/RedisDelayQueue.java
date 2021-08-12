package com.guce.queue;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import redis.clients.jedis.JedisCluster;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author chengen.gce
 * @DATE 2021/7/31 10:38 上午
 * http://redisbook.com/preview/object/sorted_set.html
 */
@Slf4j
public class RedisDelayQueue {

    private static ScheduledExecutorService executorService;

    static {
        int core = Runtime.getRuntime().availableProcessors();
        executorService = new ScheduledThreadPoolExecutor(core,
                new BasicThreadFactory.Builder().namingPattern("delay-queue-%d").daemon(false).build());
    }

    /**
     * 送zset获取第一个元素
     * 1. 如果没有获取到元素  返回nil
     * 2. 获取到元素，判断时间分数 score
     * 2.1 如果score小于 syscore 说明时间已经到了 返回元素并删除元素
     * 2.2  如果 score > sysscore 说明时间还没到  直接返回空
     */
    private static final String POP_DELAY_VAL_LUA_SCRIPT =
            "local val = redis.call('ZRANGE', KEYS[1], 0, 0) \n " +
                    "if (val == false)  then\n" +
                    "return nil ; \n" +
                    "end ; \n" +
                    "local score = redis.call('ZSCORE' , KEYS[1],val[1]) \n " +
                    "local times = tonumber(ARGV[1]) - score \n" +
                    "if (times >= 0 ) then \n" +
                    "local nextTimes = tonumber(ARGV[2]) + times \n " +
                    "redis.call('ZINCRBY' , KEYS[1], tonumber(nextTimes) , val) \n" +
                    "return val[1] ; \n " +
                    "end ; \n " +
                    "return nil ;";
    /**
     * 从队列中将消息pop出来 同时放到 延时队列中 为了保证数据一定会被处理到；
     */
    private static final String POP_QUEUE_MSG_LUA_SCRIPT =
            "local val = redis.call('rpop', KEYS[1]) \n " +
                    "if (val == false) then \n" +
                    "return nil ; \n" +
                    "end ; \n" +
                    "redis.call('ZADD', KEYS[2],tonumber(ARGV[1]),val) \n " +
                    "return val ;";

    private static final String QUEUE_SIZE_SCRIPT =
            "local listSize = redis.call('llen', KEYS[1]) \n" +
                    "local zsetSize = redis.call('zcard', KEYS[2])  then \n" +
                    "if (listSize == false and zsetSize == false)\n " +
                    "return 0 \n " +
                    "end \n " +
                    "return zsetSize;";

    private static final Long DEFAULT_INTERVAL_TIMS = 60L;

    private static final Long DEFAULT_DELAY_TIME = 5L;

    private final static String ZSET_DELAY_PREFIX = "DELAY_Z:";

    @Setter
    @Getter
    private JedisCluster jedisCluster;

    public <V> V popQueueVal(String key) {
        if (jedisCluster == null) {
            log.warn("jedisCluster is null ");
            throw new IllegalArgumentException("jedisCluster is null");
        }
        return popQueueVal(jedisCluster, key, DEFAULT_INTERVAL_TIMS);
    }

    public <V> V popQueueVal(JedisCluster jedisCluster, String key) {
        return popQueueVal(jedisCluster, key, DEFAULT_INTERVAL_TIMS);
    }

    /**
     * 从队列中获取数据
     *
     * @param jedisCluster
     * @param key
     * @param interval     间隔时间：间隔这个时间后会重试 确保  at least once
     * @param <V>
     * @return
     */
    public <V> V popQueueVal(JedisCluster jedisCluster, String key, Long interval) {

        String delayKey = generateKey(key);
        Long seconds = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        seconds += interval;

        V val = (V) jedisCluster.eval(POP_QUEUE_MSG_LUA_SCRIPT, Lists.newArrayList(key, delayKey)
                , Lists.newArrayList(seconds.toString()));
        return val;
    }

    public <V> V popDelayQueueVal(String key) {

        if (jedisCluster == null) {
            log.warn("jedisCluster is null ");
            throw new IllegalArgumentException("jedisCluster is null");
        }

        return popDelayQueueVal(jedisCluster, key);
    }

    public <V> V popDelayQueueVal(String key, Long interval) {
        if (jedisCluster == null) {
            log.warn("jedisCluster is null ");
            throw new IllegalArgumentException("jedisCluster is null");
        }
        return popDelayQueueVal(jedisCluster, key, interval);
    }

    public <V> V popDelayQueueVal(JedisCluster jedisCluster, String key) {

        return popDelayQueueVal(jedisCluster, key, DEFAULT_INTERVAL_TIMS);
    }

    /**
     * 拉取延时队列 元素
     *
     * @param jedisCluster
     * @param key
     * @param interval
     * @param <V>
     * @return
     */
    public <V> V popDelayQueueVal(JedisCluster jedisCluster, String key, Long interval) {

        Long currSeconds = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        try {

            String delayKey = generateKey(key);
            V val = (V) jedisCluster
                    .eval(POP_DELAY_VAL_LUA_SCRIPT, Lists.newArrayList(delayKey)
                            , Lists.newArrayList(currSeconds.toString(), interval.toString()));
            return val;
        } catch (Exception e) {
            log.error(" redis延时队列pop数据失败: {} ", key, e);
        }

        return null;
    }

    /**
     * 删除延时队列数据
     *
     * @param key
     * @param val
     * @return
     */
    public boolean removeDelayData(String key, String val) {

        if (jedisCluster == null) {
            log.warn("jedisCluster is null ");
            throw new IllegalArgumentException("jedisCluster is null");
        }
        return removeDelayData(jedisCluster, key, val);
    }

    public boolean removeDelayData(JedisCluster jedisCluster, String key, String val) {

        String delayKey = generateKey(key);
        Long count = jedisCluster.zrem(delayKey, val);
        return count > 0;
    }

    /**
     * @param key
     * @return
     */
    public Integer queueSize(String key) {
        if (jedisCluster == null) {
            log.warn("jedisCluster is null ");
            throw new IllegalArgumentException("jedisCluster is null");
        }
        return queueSize(jedisCluster, key);
    }

    /**
     * 判断队列剩余的数量
     *
     * @param jedisCluster
     * @param key
     * @return
     */
    public Integer queueSize(JedisCluster jedisCluster, String key) {
        String delayKey = generateKey(key);

        Integer size = (Integer) jedisCluster.eval(QUEUE_SIZE_SCRIPT, Lists.newArrayList(key, delayKey), Lists.newArrayList());
        return size;

    }

    public static String generateKey(String key) {
        StringBuilder sb = new StringBuilder();
        return sb.append(ZSET_DELAY_PREFIX).append("{").append(key).append("}").toString();

    }

    public void delayHandle(Runnable runnable) {
        delayHandle(DEFAULT_DELAY_TIME, runnable);
    }

    public void delayHandle(long intervalTime, Runnable runnable) {

        executorService.scheduleAtFixedRate(runnable, intervalTime, intervalTime, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        List list = new ArrayList<>();
        
    }
}
