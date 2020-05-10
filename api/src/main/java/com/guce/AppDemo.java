package com.guce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppDemo {

    public String doSomething(){

        System.out.println("do something ......");
        return "hello world";
    }

    static class A {
        private Integer i = 10222;
        private String x;

        A(Integer i) {
            this.i = i;
        }
    }


    public static void main(String[] args) throws IOException {
        /*int n = 7;
        System.out.println(n >>> 1);

        System.out.println(System.identityHashCode("abc"));*/

        List<A> list = new ArrayList<>();
        for (int i = 0; i < 1_000_000; i++) {
            list.add(new A(i));
        }
        System.out.println("==========");
        System.in.read();
    }
}
