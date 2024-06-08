# CMS收集日志分析

## CMS收集步骤

###  CMS垃圾回收主要分为以下7个阶段

- initial-mark ： 初始标记（CMS的第一个STW阶段）标记GC Root直接引用的对象，包括：
  - 标记老年代中所有的GC Root；
  - 标记被年轻代中引用的老年代存活对象；
-  concurrent-mark： 并发标记阶段，这个阶段会遍历整个老年代并标记所有存活的对象，并发标记的特点是和应用线程同时运行。注意并不是老年代所有的对象都会被标记，因为标记的同时应用程序会改变一些对象的引用；
- concurrent-preclean： 并发预清理阶段，也是一个并发执行的阶段。前一阶段在并发运行时，一些对象的引用会发生变化，发生变化的这些引用会被JVM标记在 Dirty Card中 ， 那些从Dirty Card对象能到达的对象也会被标记，这个标记完后，Dirty Card标记会被清除，预清理阶段可以减少下一个stop-the-world 重新标记阶段的工作量。
- concurrent-abortable-preclean： 并发可中止的预清理阶段。这个阶段其实跟上一个阶段做的事情一样，也是为了减少下一个STW重新标记阶段的工作量。增加这一阶段是为了让我们可以控制这个阶段的结束时机，比如扫描多长时间（默认5秒）或者Eden区使用占比达到期望比例（默认50%）就结束本阶段。
- Final Remark： 重标记阶段（CMS的第二个STW阶段），暂停用户线程，该阶段完成标记整个老年代所有存活的对象。需要注意的是，虽然CMS只回收老年代的垃圾对象，但是这个阶段依然需要扫描新生代，因为很多GC Root都在新生代，而这些GC Root指向的对象又在老年代，这称为“跨代引用”，所以在这个阶段应该让年轻代足够干净。
- concurrent-sweep ：并发清理。
- concurrent-reset ：重置CMS算法内部数据结构，为下一阶段做准备；

## CMS耗时长实践分析

### concurrent-abortable-preclean 并发可中止预清理耗时长

#### 现象：

运维突然电话告知有台机器的CMS GC告警，出现long gc ；

#### 导致结果：

应用正常运行，无明显应用无法访问问题；

#### 临时解决方案：

#### 原因分析：

​        通过运维给分GC 日志分析看到主要是 concurrent-abortable-preclean 长，而CMS gc这个阶段应用线程也是运行，所以未出现应用无法访问问题；

​        concurrent-abortable-preclean 阶段目的是减轻 final remark 阶段（会暂停应用线程）的负担，这个阶段同样会对 dirty card 的扫描/清理，和 concurrent-preclean 的区别在于，concurrent-abortable-preclean 会重复地以迭代的方式执行，直到满足退出条件。**但是 concurrent-preclean 已经处理过 dirty card,为什么 jvm 还需要再执行一个类似的阶段呢？**

abortable-preclean 的**触发条件**配置， 

> `-XX:CMSScheduleRemarkEdenSizeThreshold=2m`（默认值），表示当 eden 内存占用超过 2mb 时才会执行 abortable-preclean，否则没有执行的必要。



abortable-preclean 的**中断条件**:

- 配置参数是`-XX:CMSScheduleRemarkEdenPenetration=50`（默认值）

  > 表示当 eden 区内存占用到达 50%时，中断 abortable-preclean，开始执行 final-remark ; 

- abortable-preclean 的**主动退出条件**配置-XX:CMSMaxAbortablePrecleanTime=5000 和 CMSMaxAbortablePrecleanLoops

  > 主要因为如果年轻代内存占用增长缓慢，那么 abortable-preclean 要长时间执行，可能因为 preclean 赶不上应用线程创造 dirty card 的速度导致 dirty card 越来越多，此时还不如执行一个 final-remark

#### 最终解决方案：

- 设置触发concurrent-abortable-preclean 的条件

  > 通过设置 `-XX:CMSScheduleRemarkEdenSizeThreshold=100m` 来设置触发concurrent-abortable-preclean 

- 调整concurrent-abortable-preclean 时长或调整Eden区比例

  > XX:CMSMaxAbortablePrecleanTime=1000     设置concurrent-abortable-preclean 时长1秒后结束；
  >
  > -XX:CMSScheduleRemarkEdenPenetration=10   设置Eden区比例在10% 后结束；

  如果concurrent-abortable-preclean 时间设置短了，可能导致最终Final mark时间长了，要知道Final mark是需要STW的 ；

- 通过让Final Remark前进行一次 young gc来降低 Final Remark

  > -XX:+CMSScavengeBeforeRemark  注意确保这Final Remark 这次引发的young gc 不要出现 promotion failed 

  使用这个参数需要注意别踩坑：

  [一个 JVM 参数引发的频繁 CMS GC](https://www.jianshu.com/p/c76afd5b0df0)

#### 总结：

### Final Remark 耗时长

> 前几天同时过来咨询说有应用程序运行10多天后就会出现long gc

<img src="/Users/chengen.gu/workspace/github/springboot-demo/MD/picture/gclog.png" alt="gclog"  />

#### 现象

程序在线上运行时间10来天后，应用程序发生了一次耗时很长的cms gc 大概有15秒，此时应用程序处于STW状态，程序都无法访问；

#### 影响

导致所有访问该应用的请求都无法访问；影响线上用户体验；

#### 临时解决方案

- 应用重启；

- 调整老年代占比，降低老年的内存大小；

  >  原先 -XX:NewRatio=3  改成 -XX:NewRatio=1 ； 新生代：老年代由原先 1:3 改成 1:1

#### 原因分析

原先以为是内存泄漏导致说长时间gc ； 但是从监控中可以看到经过CMS GC后，内存是回收了的；所以分析heap dump没分析出什么所以然来 ；

分析gc log 发现在CMS-GC时 `final remark` 耗时很长，整个CMS过程就算他时间最长花了 40.5 s；

```text
2020-09-14T15:02:14.773+0800: 262550.148: [GC (Allocation Failure) 2020-09-14T15:02:14.773+0800: 262550.148: [ParNew: 292225K->5384K(306688K), 0.0080812 secs] 1913661K->1626821K(2063104K), 0.0082840 secs] [Times: user=0.03 sys=0.00, real=0.01 secs]

CMS: abort preclean due to time 2020-09-14T15:02:15.293+0800: 262550.667: [CMS-concurrent-abortable-preclean: 4.087/5.007 secs] [Times: user=5.12 sys=0.00, real=5.01 secs]

2020-09-14T15:02:15.311+0800: 262550.685: [GC (CMS Final Remark) [YG occupancy: 20470 K (306688 K)]2020-09-14T15:02:15.311+0800: 262550.685: [Rescan (parallel) , 0.0100449 secs]2020-09-14T15:02:15.321+0800: 262550.695: [weak refs processing, 0.0126935 secs]2020-09-14T15:02:15.334+0800: 262550.708: [class unloading, 40.4731903 secs]2020-09-14T15:02:52.807+0800: 262588.181: [scrub symbol table, 3.7824486 secs]2020-09-14T15:02:56.589+0800: 262591.964: [scrub string table, 0.0028450 secs][1 CMS-remark: 1621436K(1756416K)] 1641906K(2063104K), 9.3046642 secs] [Times: user=0.60 sys=0.00, real=40.50 secs]

2020-09-14T15:02:56.616+0800: 262591.991: [CMS-concurrent-sweep-start]
```

我们发现这个应用出现CMS GC的时的特点：

- 应用一般是10来天才进行一次CMS GC ；
- CMS GC主要耗时在 Final Remark 中 class unloading 耗时很长；

那么为何会出现 class unloading耗时很长呢 ？ 带着这个疑问通过到网上查了相关资料发现说可能是服务器swap没有关闭；

**swap的作用**

swap分区 即交换区(磁盘开辟的一部分空间)：当系统物理内存不足时，系统会将一部分长期不用的内存释放出来存到交换区中；但是其实及时内存足够，如果有一部分内存长期不使用，也会被放到交换区中；

那么我们就可以理解了，因为CMS-GC时10多天才触发一次；而且应用的特质是ToC的应用，一般都是使用一次就不在使用的，所以进入到老年代的对象长期不使用导致这部分对象被交换到 swap中了；而 Final Remark时就会到Swap中去搜索对象此时也就是到磁盘去找对象，磁盘找对象就导致很慢了；

#### 解决方案

- 内存充足的情况下，可以使用swapoff关闭掉swap 。

- 尽量不使用swap，具体来说就是降低系统参数swappiness的值(默认0-100)。此数值在Linux上默认为60，CentOS上默认为30。可以降低到10或者1，甚至是0，需要注意的是哪怕是0，也不会完全关闭掉swap，只会在内存低水位时不使用swap，如果内存到高水位（cat /proc/zoneinfo）比如到95%以上，还是会使用swap交换内存到磁盘。

```ssh
$ cat /proc/sys/vm/swappiness
30
## 临时性修改：
$ sudo sysctl vm.swappiness=10
## 永久性修改：
## 在/etc/sysctl.conf 文件里添加如下参数：
vm.swappiness=10
```

