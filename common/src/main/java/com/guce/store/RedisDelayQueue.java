package com.guce.store;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import redis.clients.jedis.JedisCluster;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @Author chengen.gce
 * @DATE 2021/7/31 10:38 上午
 * http://redisbook.com/preview/object/sorted_set.html
 */
@Slf4j
public class RedisDelayQueue {

    ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("delay-queue-%d").daemon(true).build());

    /**
     * 送zset获取第一个元素
     *  1. 如果没有获取到元素  返回nil
     *  2. 获取到元素，判断时间分数 score
     *      2.1 如果score小于 syscore 说明时间已经到了 返回元素并删除元素
     *      2.2  如果 score > sysscore 说明时间还没到  直接返回空
     */
    private static final String POP_DELAY_VAL_LUA_SCRIPT =
                    "local val = redis.call('zrange', KEYS[1], 0, 0) \n " +
                    "if (val == nil) \n" +
                    "then \n " +
                    "return nil ; \n" +
                    "end ; \n" +
                    "local score = redis.call('zscore' , KEYS[1],val) \n " +
                    "local times = ARGS[1] - score \n" +
                    "if (times >= 0 ) \n" +
                    "then \n " +
                    "local nextTimes = ARGS[2] + times \n " +
                    "redis.call('ZINCRBY' , KEYS[1], nextTimes , val) \n" +
                    "return val ; " +
                    "end ;" +
                    "return nil ;" ;
    /**
     * 从队列中将消息pop出来 同时放到 延时队列中 为了保证数据一定会被处理到；
     */
    private static final String POP_QUEUE_MSG_LUA_SCRIPT =
            "local val = redis.call('rpop', KEYS[1]) \n " +
                    "if (val == nil) \n" +
                    "then \n " +
                    "return nil ; \n" +
                    "end ; \n" +
                    "redis.call('ZADD', KEYS[2],ARGS[1],val) \n " +
                    "return val ;" ;

    private static final Long DEFAULT_INTERVAL_TIMS = 60L ;

    private final static String ZSET_DELAY_PREFIX = "DELAY_Z:";


    public <V> V popQueueVal (JedisCluster jedisCluster ,String key){
        return popQueueVal(jedisCluster,key,DEFAULT_INTERVAL_TIMS);
    }

    public <V> V popQueueVal (JedisCluster jedisCluster ,String key,Long interval) {

        String delayKey = generateKey(key);
        Long seconds  = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        seconds += interval;

        V val = (V) jedisCluster.eval(POP_QUEUE_MSG_LUA_SCRIPT,Lists.newArrayList( key , delayKey )
                ,Lists.newArrayList(seconds.toString()));
        return val;
    }
    public <V> V popDelayVal (JedisCluster jedisCluster , String key ){

        return popDelayVal(jedisCluster,key,DEFAULT_INTERVAL_TIMS);
    }

    public <V> V popDelayVal (JedisCluster jedisCluster , String key,Long interval ){

        Long currSeconds = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        try{

            String delayKey = generateKey(key);
            V val = (V) jedisCluster
                    .eval(POP_DELAY_VAL_LUA_SCRIPT, Lists.newArrayList(delayKey)
                            , Lists.newArrayList(currSeconds.toString(),interval.toString()));
            return val;
        }catch (Exception e){
            log.error(" redis延时队列pop数据失败: {} ",key,e);
        }

        return null;
    }

    public boolean remove(JedisCluster jedisCluster ,String key ,String val) {

        String delayKey = generateKey(key);
        Long count = jedisCluster.zrem(delayKey,val);
        return count > 0;
    }

    public static String generateKey(String key){
        StringBuilder sb = new StringBuilder();
        return sb.append(ZSET_DELAY_PREFIX).append("{").append(key).append("}").toString();

    }

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
        double d = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        System.out.println(d);
    }
}
