package com.guce.queue;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;

/**
 * Created by chengen.gu on 2018/10/31.
 */
public class DelayQueueDemo {

    private static BlockingQueue<DelayedInfo> queue = new DelayQueue();

    public static class DelayProdThread implements Runnable{

        @Override
        public void run() {
            DelayedInfo delayedInfo = new DelayedInfo("delayed",3000);
            queue.add(delayedInfo);
        }
    }


    public static class DelayConsumerThread implements Runnable{

        @Override
        public void run() {
            DelayedInfo delayedInfo = null;
            try {
                delayedInfo = queue.take();
                System.out.println(delayedInfo.getItem());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        }
    }

    public static void main(String[] args) {

        DelayConsumerThread consumerThread = new DelayConsumerThread();
        Thread th = new Thread(consumerThread);
        th.start();

        DelayProdThread th2 = new DelayProdThread();
        Thread th3 = new Thread(th2);

        th3.start();
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
