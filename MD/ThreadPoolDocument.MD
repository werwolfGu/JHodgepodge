#线程池

## 创建线程池方式
原始创建线程池

```java
ExecutorService executorService = new ThreadPoolExecutor(coreCpu, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES, new LinkedBlockingQueue<>(capacity)
                , r -> {

            String name = threadNamePrefix + threadNumber.getAndIncrement();
            Thread t = new Thread(r, name);
            if (t.isDaemon()){
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY){
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        });
```

- coreCPU ：核心线程数
- maximumPoolSize ：最大线程数
- keepAliveTime： realThreadNum - coreCPU 的线程存有时间;
- queue: 队列：LinkedBlockingQueue
- ThreadFactory ：线程池工厂
- RejectedExecutionHandler：超出的线程拒绝执行处理策略
    - java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy：直接执行 r.run()
    - java.util.concurrent.ThreadPoolExecutor.AbortPolicy：抛异常上去  `默认策略`
    - java.util.concurrent.ThreadPoolExecutor.DiscardPolicy：什么都不敢，直接丢弃
    - java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy：丢弃最后的一个，执行该线程