# 分布式锁实现方式
    常用分布式锁实现方式：redis、zookeeper、数据表主键锁；本文介绍的是redis分布式锁的实现方式；
# redis 分布式可重入锁实现
   重入锁：一个线程获得了锁之后仍然可以**反复**的加锁，不会出现自己阻塞自己的情况。
## 基于set (key,value,EXIST,expx,timeout)方式实现
    该命令保证了原子性同时设置了过期时间防止死锁
### nxxx 
    NX:当key不存在时 设置value值；
    XX: 当key存在时 设置value值；
### expx
    EX:超时单位为second 
    PX：milliseconds
### timeout
    配合上面的超时单位 设置超时时间
## 可重入锁实现
    在实现可重入时，加入了对象
```java
private static class LockData{

        final Thread owningThread;
        final AtomicInteger lockCount = new AtomicInteger(1);

        private LockData(Thread owningThread)
        {
            this.owningThread = owningThread;
        }
    }
```
- owningThread:锁的线程拥有者
- lockCount:当前线程锁的次数

以下是加锁的方式
```java
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
        Long currTime ;

        LockData lockData = lockDataMap.get(currentThread);
        if(lockData != null){
            lockData.lockCount.incrementAndGet();
            return true;
        }

        final long      startMillis = System.currentTimeMillis();
        final Long      millisToWait = (unit != null) ? unit.toMillis(time) : null;

        while (true){

            currTime = System.currentTimeMillis();

            String result = jedisCommands.set(key,currTime.toString(),SET_IF_NOT_EXIST,SET_WITH_EXPIRE_TIME,overtime);

            //加锁  判断是否成功
            if(LOCK_SUCCESS.equals(result)){

                LockData newLockData = new LockData(currentThread);
                lockDataMap.put(currentThread, newLockData);

                return true;
            }

            if(millisToWait != null){

                long currSysTime = System.currentTimeMillis();
                if(currSysTime - startMillis > millisToWait){
                    return false;
                }
            }else{
                //防止cpu空转
                sleep();
            }

        }
    }
```

- 解锁使用del(key) 来实现 但是不是随便随都能解锁；此时LockData也在此起作用了 如果不是该线程加的锁，是不让解锁的
```java
    @Override
    public boolean unlock(JedisCommands jedisCommands, String key) {

        Thread currentThread = Thread.currentThread();
        LockData lockData = lockDataMap.get(currentThread);
        if ( lockData == null )
        {
            throw new IllegalMonitorStateException("You do not own the lock: " + key + " currThread:" + currentThread );
        }
        int newLockCount = lockData.lockCount.decrementAndGet();
        if ( newLockCount > 0 )
        {
            return true;
        }

        if ( newLockCount < 0 )
        {
            throw new IllegalMonitorStateException("Lock count has gone negative for lock: " + key);
        }

        try{

            jedisCommands.del(key);
        }finally {
            lockDataMap.remove(currentThread);
        }

        return true;
    }
```
- [源代码](https://github.com/werwolfGu/JHodgepodge/blob/master/api/src/main/java/com/guce/redis/lock/impl/InterProcessRedisMutexLock.java)
- [示例](https://github.com/werwolfGu/JHodgepodge/blob/master/api/src/test/java/com/guce/redis/lock/LockTest.java)
## 总结
- 至此redis可重入锁实现已完成，但是依然还有问题存在，比如业务代码还未执行完，但是redis超时时间一到，所以这个超时时间需要业务自己控制；

如有未考虑周全的地方请加以指正；
