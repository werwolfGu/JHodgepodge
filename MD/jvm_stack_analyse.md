# JVM stack分析
    平时系统运行过程中，会突然cpu load出现飚高的情况，线程数过多等各种各样的情况；jvm堆栈信息分析；
    
堆栈信息分析使用命令 jstack

- 线程状态
>线程的状态：new/runnable/running/waiting/timed_waiting/blocked/dead

    new :当创建一个线程对象，此时改线程还无法执行，会出现此状态；
    
    runnable：当执行线程thread.start()时，进入可执行状态；只等分配CPU来执行了；
    
    running：线程分配到CPU开始执行；
    
    waiting：线程进入等待状态 调用thread.join()或锁对象调用obj.wait() 时会进入该状态；表明在等待某个资源或某个条件唤醒；
    
    timed_waiting：线程进入定时等待状态 当调用thread.join(time) 、Thread.sleep(time)、obj.wait(time),现在在指定的时间内等待资源或等待出发条件唤醒自己；
    
    blocked：如果进入同步方法或同步代码块，没有获取到锁，则会进入该状态；
    
    dead：线程执行完毕，或出现未捕获的Exception ；表明该线程已经结束；

>同时请注意：wait和sleep的区别 ：wait是会释放锁的；但是sleep是不会释放锁的；
在jstack时 我们还要重点关注一下信息：
Deadlock：表示死锁；
Waiting on condition：等待某个资源或条件触发唤醒自己。具体可结合jstacktrace来分析，比如线程正在sleep，网络读写繁忙而等待；
Blocked：阻塞；
Waiting on monitor entry：在等待获取锁；
in Object.wait()：线程获取锁之后又执行obj.wait()放弃锁；

- 我们如何获取到jvm的堆栈信息

    - 获取进程号：jps -v 或  ps -ef|grep -v grep | grep tomcat ;
    - 获取进程的堆栈信息： jstack -l pid  >> jvmstack.txt   或 kill -3 pid;
    - 如果我们要查看进程中的哪个线程占用CPU高 可通过  top -c -Hp pid ;

- top -Hp ${pid} 如下图所示

![](https://github.com/werwolfGu/JHodgepodge/blob/master/web/src/main/webapp/picture/top-Hp.png)

>这个time+ 表示该线程耗费的CPU时间

- ps H -eo user,pid,ppid,tid,time,%cpu,cmd --sort=%cpu | more
>查看cpu消耗最高线程

通过 `jstack -l pid  >> jvmstack.txt`   或 `kill -3 pid` 获取到的堆栈信息和上面获取到的线程号( `printf "%x\n" pid`)转成16进制找到对应的线程；找到对应的线程；再具体分析该线程；


![](https://github.com/werwolfGu/JHodgepodge/blob/master/web/src/main/webapp/picture/jstack.png)