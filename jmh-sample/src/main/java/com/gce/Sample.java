package com.gce;

import java.util.concurrent.TimeUnit;

/**
 * @Author chengen.gu
 * @DATE 2020/2/5 7:16 下午
 */
public class Sample {

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("cost time:" + (System.currentTimeMillis() - start));
    }
}
