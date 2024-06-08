package com.guce.service;

import com.guce.annotation.LoggerTrace;
import org.springframework.stereotype.Component;

/**
 * @Author chengen.gce
 * @DATE 2022/9/9 01:10
 */
@Component
@LoggerTrace
public class DemoManager {

    public void test(){
        System.out.println("test");
    }
}
