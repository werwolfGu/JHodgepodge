package com.guce.log4j;

import com.alibaba.ttl.threadpool.TtlExecutors;
import org.slf4j.MDC;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author chengen.gu
 * @DATE 2020/2/17 8:41 下午
 */
public class MDCDemo {

    static {
        System.setProperty("log4j2.threadContextMap","com.guce.log4j.ExtendThreadContextMap");
    }
    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        //TransmittableThreadLocal<String> context = new TransmittableThreadLocal<String>();
        executorService = TtlExecutors.getTtlExecutorService(executorService);

        //context.set("alibaba ttl->-1");
        for (int i = 0 ; i < 10 ; i++) {
            MDC.put("ThreadId","MDC->" + i);

            executorService.submit( () -> {
                System.out.println(Thread.currentThread().getName() +"inner:" + MDC.get("ThreadId"));

                //executorService2.submit( () -> System.out.println( Thread.currentThread().getName() + "inner2:" + MDC.get("ThreadId")));
            });
        }


        System.out.println("outter:" + MDC.get("ThreadId"));
    }
}
