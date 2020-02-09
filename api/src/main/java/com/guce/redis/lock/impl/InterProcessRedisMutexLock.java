package com.guce.redis.lock.impl;

import com.guce.redis.lock.InterProcessLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.commands.JedisCommands;
import redis.clients.jedis.params.SetParams;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * redis 实现分布式可重入锁
 * 利用 redis set (key,value,NX,PX,timeout)  实现redis原子性操作
 * NX 表示 key不存在时才设置key的值
 * XX 表示 key存在才设置 key value 值
 * PX 超时间单位是 ms
 */
public class InterProcessRedisMutexLock implements InterProcessLock {

    private final static Logger logger = LoggerFactory.getLogger(InterProcessRedisMutexLock.class);

    private final static long overtime = 5 * 60 * 1000L;  //超时5分钟
    private final static long sleeptime = 3;   // ms

    private final static String LOCK_SUCCESS = "OK";
    private final static String SET_IF_NOT_EXIST = "NX";      //只有key不存在时才设置key的值
    private final static String SET_WITH_EXPIRE_TIME = "PX";  //设置过期时间单位  ms


    private final ConcurrentMap<Thread, LockData> lockDataMap = new ConcurrentHashMap<>();

    @Override
    public boolean lock(JedisCommands jedis, String key) throws InterruptedException {

        return tryLock(jedis, key, overtime, null);
    }

    /**
     * @param jedisCommands
     * @param key
     * @param time
     * @param unit
     * @return
     * @throws InterruptedException
     */
    @Override
    public boolean tryLock(JedisCommands jedisCommands, String key, long time, TimeUnit unit) throws InterruptedException {

        Thread currentThread = Thread.currentThread();
        Long currTime;

        LockData lockData = lockDataMap.get(currentThread);
        if (lockData != null) {
            lockData.lockCount.incrementAndGet();
            return true;
        }

        final long startMillis = System.currentTimeMillis();
        final Long millisToWait = (unit != null) ? unit.toMillis(time) : null;

        while (true) {
            currTime = System.currentTimeMillis();
            String result = null;
            try {
                SetParams setParams = new SetParams();
                setParams.nx()
                        .px(overtime);
                result = jedisCommands.set(key, currTime.toString(), setParams);
            } catch (Exception e) {
                logger.error("redis lock Exception key:{}  -> message:{}", key, e.getMessage(), e);
            }
            //加锁  判断是否成功
            if (LOCK_SUCCESS.equals(result)) {

                LockData newLockData = new LockData(currentThread);
                newLockData.setOverTime(overtime);
                lockDataMap.put(currentThread, newLockData);

                return true;
            }

            if (millisToWait != null) {

                long currSysTime = System.currentTimeMillis();
                if (currSysTime - startMillis > millisToWait) {
                    return false;
                }
            } else {
                //防止cpu空转
                sleep();
            }

        }
    }

    @Override
    public boolean unlock(JedisCommands jedisCommands, String key) {

        Thread currentThread = Thread.currentThread();
        LockData lockData = lockDataMap.get(currentThread);
        if (lockData == null) {
            throw new IllegalMonitorStateException("You do not own the lock: " + key + " currThread:" + currentThread);
        }
        int newLockCount = lockData.lockCount.decrementAndGet();
        if (newLockCount > 0) {
            return true;
        }

        if (newLockCount < 0) {
            throw new IllegalMonitorStateException("Lock count has gone negative for lock: " + key);
        }
        try {

            //如果已经超时了还去删除的话，可能会把其他线程的锁给删了
            long overTime = lockData.getOverTime();
            if (overTime > 0) {
                long currTime = System.currentTimeMillis();
                if (overTime > (currTime - lockData.getCurrTime())) {
                    lockDataMap.remove(currentThread);
                    return true;
                }
            }
            jedisCommands.del(key);
        } finally {
            lockDataMap.remove(currentThread);
        }

        return true;
    }

    private void sleep() {

        try {
            Thread.sleep(sleeptime);
        } catch (InterruptedException e) {
            logger.error("redis lock Thread sleep InterruptException :{}", e.getMessage(), e);
        }
    }

    private static class LockData {

        final Thread owningThread;
        private Long currTime = System.currentTimeMillis();
        private Long overTime = 0L;
        final AtomicInteger lockCount = new AtomicInteger(1);

        private LockData(Thread owningThread) {
            this.owningThread = owningThread;
        }

        public void setOverTime(Long overTime) {
            this.overTime = overTime;
        }

        public long getOverTime() {
            return overTime;
        }

        public long getCurrTime() {
            return currTime;
        }
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.144.122", 6379);
//        String result = jedis.set("ab","1234",SET_IF_NOT_EXIST,SET_WITH_EXPIRE_TIME,overtime);
//        System.out.println(result);
        System.out.println(jedis.get("lock"));

    }
}
