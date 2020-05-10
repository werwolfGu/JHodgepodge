package com.guce.thread;

import java.util.concurrent.locks.LockSupport;

/**
 * @Author chengen.gce
 * @DATE 2020/4/19 2:35 下午
 */
public class LockSupportDemo {
    private static Thread t1 = null, t2 = null;

    public static void main(String[] args) {
        char[] ch1 = "1234567".toCharArray();
        char[] ch2 = "ABCDEFG".toCharArray();
        t1 = new Thread(() -> {
            for (char ch : ch1){
                LockSupport.park();
                System.out.println(ch);
                LockSupport.unpark(t2);
            }
        },"t1");

        t1 = new Thread(() -> {
            for (char ch : ch1){

                System.out.println(ch);
                LockSupport.unpark(t1);
                LockSupport.park();
            }
        },"t2");


    }
}
