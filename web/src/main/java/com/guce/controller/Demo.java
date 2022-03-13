package com.guce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sync")
public class Demo {

    private static final Object lock = new Object();

    public void test() {
        synchronized (lock) {
            for (int i = 0; i < 10; i++) {
                System.out.println("sync ->" + i);
            }
        }
    }
}
