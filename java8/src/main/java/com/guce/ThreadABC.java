package com.guce;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author chengen.gce
 * @DATE 2021/8/18 3:53 下午
 */
public class ThreadABC {

    private ReentrantLock lock = new ReentrantLock();
    private Condition a = lock.newCondition();
    private Condition b = lock.newCondition();
    private Condition c = lock.newCondition();
    private  volatile int flag = 0 ;

    public class A extends Thread{

        @Override
        public void run() {
            while (true) {
                lock.lock();
                try{
                    while (true) {
                        while(flag != 0){
                            a.wait();
                        }
                        System.out.println("A do something...");
                        Thread.sleep(1000L);
                        flag = 1;
                        b.notify();

                    }
                }catch (Exception e){

                }finally {
                    lock.unlock();
                }

            }
        }
    }

    public class B extends Thread{

        @Override
        public void run() {
            while (true) {
                lock.lock();
                try{
                    while (true) {
                        while(flag != 1){
                            b.wait();
                        }

                        System.out.println("B do something...");
                        Thread.sleep(1000L);
                        flag = 2;
                        c.notify();

                    }
                }catch (Exception e){

                }finally {
                    lock.unlock();
                }

            }
        }
    }

    public class C extends Thread{

        @Override
        public void run() {
            while (true) {
                lock.lock();
                try{
                    while (true) {
                        while(flag != 2) {
                            c.wait();
                        }
                        System.out.println("C do something...");
                        Thread.sleep(1000L);
                        flag = 0;
                        a.notify();

                    }
                }catch (Exception e){

                }finally {
                    lock.unlock();
                }

            }
        }
    }

    public void run(){
        A a = new A();
        B b = new B();
        C c = new C();

        a.start();
        b.start();
        c.start();

    }

    public static void main(String[] args) {
        ThreadABC abc = new ThreadABC();
        abc.run();

    }
}

