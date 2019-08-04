# java队列
   在java.util.concurrent包下提供的阻塞队列包括：
-   ArrayBlockingQueue/LinkedBlockingQueue

    LinkedBlockingQueue无界；ArrayBlockingQueue有界指定队列大小
-   PriorityBlockingQueue

    优先级队列
-   DelayQueue

    基于PriorityQueue实现的时间调度队列；根据延时时间设置
-   SynchronousQueue

    同步消费队列；消费一个生产一个；不消费不生产；

