package com.guce.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author chengen.gce
 * @DATE 2020/3/1 9:23 下午
 */
public class ThreadPoolDemo {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        int i = 2 ,j = 2;
        if (i ==2 || ((j = 4) != 3)){
            System.out.println(" j->" + j);
        }
    }
}
