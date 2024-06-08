package com.guce;

import org.openjdk.jol.info.ClassLayout;

/**
 * @Author chengen.gce
 * @DATE 2022/4/16 6:55 PM
 */
public class Hello_JOL {

    public static void main(String[] args) {
        Object obj = new Object();
        try {Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        synchronized (obj){

            System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        }
    }
}
