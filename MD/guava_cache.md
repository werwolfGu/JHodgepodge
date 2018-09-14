# 缓存
       缓存在日常开发过程中起到举住轻重的作用；如果你对某些热点数据频繁的读取，并且改动比较不大时就非常适合使用缓存来提高性能。
    但是也不是一本万利的，读取快了因为我们使用的是宝贵的内存资源，也是典型的用空间换时间；
- Java中使用到的缓存
    - JVM缓存： 常见的有 `JVM CACHE`  `guava cache`  `ehcache`；
    - 分布式缓存：常见的比如有`redis` `memcache` 等；

## JVM缓存
    JVM缓存也可以认为是堆缓存 最简单的就是创建全局变量 比如 Map,List 之类的容器，将数据存放在其中。
    
- 这缓存的优势是简单方便，但是也会出现以下问题：
    - 只能显示的写入、读入；
    - 不能按照一定的规则淘汰数据 如 `LRU` `LFU` `FIFO` ;
    - 清除回调通知；
    - 高并发下可能出现 `缓存穿透` `缓存雪崩` `缓存击穿` 等问题；
    - 其他的一些定制功能等；

### Ehcache、Guava Cache
    现在市面上也有一些专门用作JVM缓存的开源框架比如：Ehcache、Guava Cache ;
    它具备上面提到的缓存不存在的功能：如：定时刷新、自动清除数据、按规则淘汰数据等；
## 分布式缓存
    上面提到的是JVM堆内缓存，只能在各自线程或进程中执行；这样在分布式环境下就无法使用了；
    在分布式环境下我们要共享内存的话，我们就可以使用比如： redis、memcache来存储；
## JVM缓存-Guava Cache 分析
    Guava Cache是google开源的Java工具集库中的一款缓存工具。Guava Cache借鉴了ConcurrentHashMap的设计思路，
    使用多个segments方式细粒度锁，保证在线程安全的同事，在高并发场景下也能很好性能；
- 实现功能
    - 自动将Entry节点加载到缓存结构中；
    - 当缓存的数量超过设置的maxSize时，会根据LRU算法淘汰数据；
    - 具备根据Entry节点上次访问时间计算过期时间；
    - 具备根据Entry节点根据上次访问时间重新刷新数据；
    - 缓存key被封装在WeakReference引用中；
    - 缓存Value被封装在WeakReference或SoftReference引用中；
- 如图所示，guava cache结构图
![](https://github.com/werwolfGu/JHodgepodge/blob/master/web/src/main/webapp/picture/guava-cache.png)



- [简单cache实现示例](https://github.com/werwolfGu/JHodgepodge/blob/master/service/src/main/java/com/guce/cache/SimpleExample.java)    



    