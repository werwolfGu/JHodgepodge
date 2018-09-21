一致性hash
=========
    我们平时业务开发过程中经常会为了减轻数据库、分布式缓存的压力而部署多个节点；为了将数据均匀的分布到各个节点；我们一般会使用hash来定位到某个节点；
    遇到比如对数据库进行**分表分库** 、分布式缓存节点**增加/删除/宕机**的情况；但是在普通的hash 
    
- 如图所示
![](https://github.com/werwolfGu/JHodgepodge/blob/master/web/src/main/webapp/picture/hash_1.png)    
    
对于分布式缓存不同机器上存着不同的数据；为了实现这些缓存节点的负载均衡，使用hash定位服务器是很常见的一种方式
>m=hash(o)%n  o：需要缓存的对象；n：缓存节点的数量；m：定位到一台缓存节点的编号；比如m=2定位到database-B;
这个算法在平时大部分时间下是没问题；但是比如当需要缓存的数据量增大了，需要增加服务器节点；或某个节点宕机了；比如此时n=4那么hash出来的只就完全不一致了；影响到的数据就很多了；
在这种情况下一致性hash就出现了；

- 一致性hash

一致性hash是对2^32是模，所以起点是0，终点是2^32-1;并且[0,2^23-1]形成一个环；    
如图所示

![](https://github.com/werwolfGu/JHodgepodge/blob/master/web/src/main/webapp/picture/hash_3.png)
    
将对象放入到hash环中
如图所示

![](https://github.com/werwolfGu/JHodgepodge/blob/master/web/src/main/webapp/picture/hash_4.png)    
>m1=hash(o3);
m2=hash(o1);
m3=hash(o2);    

将节点放入到hash环中
如图所示

![](https://github.com/werwolfGu/JHodgepodge/blob/master/web/src/main/webapp/picture/hash_5.png)
>n1=hash(c3);
n2=hash(c1);
n3=hash(c2);
一般使用机器IP或机器名称来做hash；

对象如何选机器
通过顺时针找到最近的一台服务器将数据加入到那个节点上；

增减节点的情况
增加节点如图所示

![](https://github.com/werwolfGu/JHodgepodge/blob/master/web/src/main/webapp/picture/hash_6.png)
>从图中可以看出当增加节点后受影响的数据只有一小部分节点 n2~n4位置的数据会访问到n4 ；其他数据还是正常访问的；

减少节点如图所示

![](https://github.com/werwolfGu/JHodgepodge/blob/master/web/src/main/webapp/picture/hash_7.png)
>减少节点后如图所示o2会访问到n2上，即原先n1~n3节点的数据都会去访问n2节点了；    

- 虚拟节点

>上面已基本上介绍完了一致性hash的基本原理了，但是还有一个问题，比如环上的节点分布不均衡；比如增加一个节点但是影响的
只是增加节点的后面的那个节点其他节点不受影响；为此引入了虚拟节点来解决分布不均衡的问题；
将每台物理机虚拟成一组虚拟机，将虚拟机放置到hash环上，如果需要确定对象的机器，先确定对象的虚拟机器，再由虚拟机器确定物理机器。

如图所示

![](https://github.com/werwolfGu/JHodgepodge/blob/master/web/src/main/webapp/picture/hash_2.png)

>如图,c1 c2  c3分别分出了一组虚拟节点出来
c1->c_11、c_12、c_13;
c2->c_21、c_22、c_23;
c3->c_31、c_32、c_33;

- [一致性hash示例](https://github.com/werwolfGu/JHodgepodge/blob/master/app-leetcode/src/main/java/com/guce/ConsistentHashWithoutVN.java)


