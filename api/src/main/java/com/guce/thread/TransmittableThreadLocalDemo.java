package com.guce.thread;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author chengen.gu
 * @DATE 2020/2/17 4:46 下午
 *
 * JDK的InheritableThreadLocal类可以完成父线程到子线程的值传递。但对于使用线程池等会池化复用线程的执行组件的情况，
 * 线程由线程池创建好，并且线程是池化起来反复使用的；这时父子线程关系的ThreadLocal值传递已经没有意义，应用需要的实际
 * 上是把 任务提交给线程池时的ThreadLocal值传递到 任务执行时。
 *
 *
 * 对这句话的理解： 对于线程池创建完成后ThreadLocal 已经创建好了 ；下次请求过来时；由于线程池化了，线程的ThreadLocal不会再被赋予父线程的ThreadLocal的值；
 */
public class TransmittableThreadLocalDemo {


    private  static TransmittableThreadLocal<String> context = new TransmittableThreadLocal<String>();

    public static void main(String[] args) {
        context.set(" hello transmittalbe ");
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService = TtlExecutors.getTtlExecutorService(executorService);



        for (int i = 0 ; i < 10 ; i++){

            executorService.submit( () -> {
                System.out.println(Thread.currentThread().getName() + "  ;" );

            });
            context.set(" hello transmittalbe " + i);
        }


        System.out.println("outter:" + context.get());
        context.remove();
    }
}
