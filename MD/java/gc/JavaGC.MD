# JavaGC实战

## JVM内存模型

### 内存结构介绍

当前主流JVM(hotspot VM)将内存划分为不同的物理区，就是`分代`的思想；如图所示：JVM主要分为：
新生代(eden区、survivor(form/to)) 、老年代、永久代(JDK8 以后为metaspace)

![](.JavaGC.MD_images/07e6cc71.png)

- 新生代(Young Generation):大多数对象在新生代创建，其中很多的对象生命周期很短暂；每次新生代垃圾回收(Minor GC)
后，只有少量的对象还存活，所以使用复制算法，只需少量的复制成本就能完成垃圾回收；

新生代又分为Eden区 、两个survivor(form 、to)区；大部分对象在Eden区创建；当Eden区满后，会触发一次`Minor GC`，将
Eden区的对象复制到survivor中的一个区中，当Survivor区满时，此时存活的对象还不满足晋升条件的对象会复制到另一个survivor
，对象没经历一次GC，年龄 `+1` ；达到`晋升年龄阈值`后会将这个对象放入`老年代`，这个过程也叫晋升；所以`晋升年龄阈值`直接
影响对象在新生代中的停留时间，在Serial和ParNew GC两种回收器中，`晋升年龄阈值`通过参数`MaxTenuringThreshold`设定，默认值为15。

- 老年代(old Generation):在新生代中的对象经过N次垃圾回收后仍然存活的对象，就会被放入老年代中，该区域的特点：大部分对象存活时间长；
老年代垃圾回收(Major GC)通常使用`标记-整理`或`标记-清理`算法；
整堆包括新生代和老年代的垃圾回收称为Full GC（HotSpot VM里，除了CMS之外，其它能收集老年代的GC都会同时收集整个GC堆，包括新生代）。
可能晋升到老年代的对象：
    - 晋升年龄达到阈值`MaxTenuringThreshold`的对象会晋升到老年代；
    - 在`Minor GC` 时存活的对象在Survivor存不下的对象的大对象会直接晋升到老年代；

- 永久代(Perm Generation):在jdk8之后是`metaspace` 主要存放元数据，例如Class、Method的元信息，与垃圾回收要回收的Java对象关系不大。相对于新生代和年老代来说，该区域的划分对垃圾回收影响比较小。

## GC触发条件

1. Minor GC触发条件：
    1. 当Eden区满，无法创建对象时，就会触发Mintor GC；
    2. Major GC时也会触发Mintor GC；
2. Major GC触发条件：
    1. 如果有FULL GC请求时会触发一次GC；
    2. UseCMSInitiatingOccupancyOnly参数配置
        1. 未配置(false)
            根据统计数据动态判断是否需要进行一次 CMS GC。判断逻辑是，如果预测 CMS GC 完成所需要的时间大于预计的老年代将要填满的时间，则进行 GC。
            第一次 CMS GC 时，统计数据还没有形成是无效的，这时会跟据 Old Gen 的使用占比来进行判断是否要进行 GC。这个占比为`50%`；
        2. 配置(true)
            根据`CMSInitiatingOccupancyFraction` 默认值是92% 判断old Gen空间使用占比情况；
    3. 判断年轻代晋升对象晋升是否可能会失败，如果会失败，则进行一次回收；
    4. 如果metaSpace认为需要回收metaSpace区域，也会触发一次cms回收
3. FULL GC触发条件
    1. Perm Gen(metaSpace)空间不足时；
    2. CMS GC出现`promotion failed`和`concurrent mode failure` 时
        1. promotion failed：在进行Minor GC时，Survivor Space放不下，对象只能放入老年代，而此时老年代也放不下造成的。
        2. concurrent mode failure：
            1. Major GC时 同时业务线程将对象放入老年代，而此时老年代空间不足时；
            2. Minor GC时 Survivor 区放不下对象；晋升到老年代，此时老年代也发不下；

## GC日志分析

GC日志如下图：

![](.JavaGC.MD_images/b326f20a.png)


通过上图GC日志中两处红色框标记内容可知： 
1. new threshold = 2（动态年龄判断，对象的晋升年龄阈值为2），对象仅经历2次Minor GC后就晋升到老年代，这样老年代会迅速被填满，直接导致了频繁的Major GC。 
2. Major GC后老年代使用空间为300M+，意味着此时绝大多数(86% = 2G/2.3G)的对象已经不再存活，也就是说生命周期长的对象占比很小。

>由此可见，服务中存在大量短期临时对象，扩容新生代空间后，Minor GC频率降低，对象在新生代得到充分回收，只有生命周期长的对象才进入老年代。这样老年代增速变慢，Major GC频率自然也会降低。


TLAB

参考

[Spring Boot引起的“堆外内存泄漏”排查及经验总结](https://tech.meituan.com/2019/01/03/spring-boot-native-memory-leak.html)  
[从实际案例聊聊Java应用的GC优化](https://tech.meituan.com/2017/12/29/jvm-optimize.html)  
[CMS之promotion failed&concurrent mode failure](https://www.jianshu.com/p/ca1b0d4107c5)