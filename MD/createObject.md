# 对象创建和内存分配
## Java对象结构
- java对象结构图如下
![](https://github.com/werwolfGu/JHodgepodge/blob/master/web/src/main/webapp/picture/Object.png)
    
    - 如图所示对象头包含：markword(_mark)和class对象指针(_klass)
        - markword占用8个字节，用于存储对象自身的运行时数据，如哈希码（HashCode）、GC分代年龄、
        锁状态标志、线程持有的锁、偏向线程ID、偏向时间戳等
        - class对象指针：占用8个字节如果开启指针压缩的话则占用4个字节，即对象指定的是那个实例对象

> 所以一个对象如果未声明任何属性的话占用的内存为16字节或2字节(开启指针压缩)开启指针压缩方式`-XX:+UseCompressedOops`默认是开启的；
实际数据对象(`instance_data`)：占用的是对象指针大小`8个字节`开启指针压缩的话就占用`4个字节`;
补齐填充(padding):对齐`8个字节`，即对象头+对象实例数据的大小要是`8的倍数`；

如下代码
```java
public class A  {
    private Long i = 1L;
   private String s1;
    /* private String s2;
    private String s3;
    private String s4;*/

}
```
>此时占用的内存大小为:`12(markWord->8class对象指针->4) +4(对象地址)+4(对象地址)=24bytes`;仔细的你肯定发现这不是等于20嘛?
是的；因为有对齐补充即地址的总和是`8的倍数`；所以这里是24； **static属性是不占堆内存的**
若未开启对象指针压缩:16 + 8 + 8 = 32;

使用java小工具： jmap -histo ${pid} 可以查看对象的大小

Integer 和int 占用的内存分别是多少呢？
Integer:16个字节  ；int 占 4个字节；



