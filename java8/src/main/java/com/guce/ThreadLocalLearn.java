package com.guce;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author chengen.gce
 * @DATE 2021/11/29 9:44 下午
 */
public class ThreadLocalLearn {
    /**
     * @see Thread 出现内存溢出的情况
     */

    private static ThreadLocal<List<String>> threadLocal = new ThreadLocal<>();

    private static Executor executor = Executors.newFixedThreadPool(500);

    /**
     * 内存溢出的原因：
     * 1. 线程池中的线程一直存在的 ；
     * 2. 此时 ThreadLocalMap 的生命周期其实跟thread是一样的；
     */
    public void threadLocalOOM() {

        for ( int i = 0 ; i < 500000; i++){
            executor.execute( () -> {

                threadLocal.set(addList());
                System.out.println("do something...");
                /**
                 * 如果没有 threadLocal.remove(); 那么Entry中的key 和value一直存在  那么线程如果没有销毁的话 就一直存在
                 * 不管key是强引用还是弱应用都是有问题的；
                 * threadlocal中的值就被遗留在内存中；如果线程池很大，导致了之前执行过的线程一直没有被执行到；
                 */

                threadLocal.remove();
            });
        }

    }

    public List<String> addList(){
        List<String> list = new ArrayList<>();
        for (int i = 0 ; i < 1000 ; i++ ){
            list.add("i" + i);
        }
        return list;
    }

}
