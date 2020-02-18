package com.grvyframework.grvy.serivce;

import org.springframework.stereotype.Service;

/**
 * @Author chengen.gu
 * @DATE 2020/2/18 7:40 下午
 */
@Service("demoService")
public class DemoService {

    public String test(String name){
        System.out.println("spring invoke.....");
        return "spring container invoke:" + name;
    }
}
