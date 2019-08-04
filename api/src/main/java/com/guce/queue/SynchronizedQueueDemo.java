package com.guce.queue;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by chengen.gu on 2018/10/31.
 */
public class SynchronizedQueueDemo {

    private static BlockingQueue<String> synchronizedQueue = new SynchronousQueue();

    public static class ProducterThread implements Runnable{

        @Override
        public void run() {
            try {
                while (true){
                    String value = Thread.currentThread().getName() + "->" + System.currentTimeMillis()/1000;
                    synchronizedQueue.put(value);
                    System.out.println("producter->" + value);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class ConsumeThread implements Runnable{

        @Override
        public void run() {
            try {
                while (true){
                    String value = synchronizedQueue.take();
                    Thread.sleep(1000);
                    System.out.println("consum ->" + value);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        for(int i = 0 ;  i< 2 ; i++ ){
            ConsumeThread pr = new ConsumeThread();
            Thread th = new Thread(pr);
            th.setName("i-" + i);
            th.start();
        }

        Thread.sleep(100);
        ProducterThread consume = new ProducterThread();
        Thread th1 = new Thread(consume);
        th1.start();

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
