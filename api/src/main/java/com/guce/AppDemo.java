package com.guce;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.atomic.AtomicInteger;

public class AppDemo {

    public String doSomething() {

        System.out.println("do something ......");
        return "hello world";
    }

    static class A {
        private Integer i = 10222;
        private String x;

        A(Integer i) {
            this.i = i;
        }
    }

    private static final AtomicInteger threadNumber = new AtomicInteger(0);

    public static final ForkJoinPool.ForkJoinWorkerThreadFactory factory = new ForkJoinPool.ForkJoinWorkerThreadFactory() {
        @Override
        public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
            final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            worker.setName("my-thread-prefix-name-" + worker.getPoolIndex());
            return worker;
        }
    };

    public static void main(String[] args) throws IOException {

        System.out.println(System.getProperty("basedir"));
    }
}
