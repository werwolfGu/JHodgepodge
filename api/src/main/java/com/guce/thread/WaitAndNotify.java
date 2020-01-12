package com.guce.thread;

/**
 * @Author chengen.gu
 * @DATE 2019/11/19 2:52 下午
 * 锁池：和等待池
 * 锁池：假设有一个线程A已经拥有某个对象的锁，而其他线程也调用了这个对象的synchronize代码块，由于这些线程必须获取对象的锁才能执行代码
 *      但是该对象的锁目前正在被线程A使用中，此时这些线程将进入锁池，等待竞争锁；
 * 等待池：假设一个线程A调用了某个对象的wait()方法，线程A就会释放该对象的锁(因为wait()方法必须出现在synchronized中，
 *      这样自然在执行wait()方法之前线程A就已经拥有了该对象的锁)，同时线程A就进入到了该对象的等待池中。
 *      如果另外的一个线程调用了相同对象的notifyAll()方法，那么处于该对象的等待池中的线程就会全部进入该对象的锁池中，准备争夺锁的拥有权。
 *      如果另外的一个线程调用了相同对象的notify()方法，那么仅仅有一个处于该对象的等待池中的线程(随机)会进入该对象的锁池.
 */
public class WaitAndNotify {
    private static Boolean flag = false;

    public static class Thread1 extends Thread {

        private Object obj;

        public Thread1(Object obj) {
            this.obj = obj;
        }

        @Override
        public void run() {

            System.out.println(Thread.currentThread().getName() + " thread into waiting");
            synchronized (obj) {
                try {
                    while (!flag) {
                        obj.wait();
                    }
                    flag = false;
                    System.out.println(Thread.currentThread().getName() + " do synchronized code");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + " thread has been notified");
        }
    }


    public static void main(String[] args) {

        Object obj = new Object();
        for (int i = 0; i < 5; i++) {
            Thread th = new Thread1(obj);
            th.setName("th->" + i);
            th.start();
        }
        try {
            Thread.sleep(10);
            System.out.println("===============================");
            synchronized (obj) {
                flag = true;
                obj.notifyAll();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
