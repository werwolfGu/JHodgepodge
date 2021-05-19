package com.guce;


import co.paralleluniverse.fibers.Fiber;

/**
 * @Author chengen.gce
 * @DATE 2020/4/6 9:15 下午
 */
public class QuasarDemo {

    public static void main(String[] args) {


        for (int i = 0 ; i < 1000 ; i++ ){
            Fiber fiber = new Fiber( () -> {
                System.out.println("hello fiber");
            });
            fiber.start();
        }

        System.out.println(1<< 2);

    }
}
