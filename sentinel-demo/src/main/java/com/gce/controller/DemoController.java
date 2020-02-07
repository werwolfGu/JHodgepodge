package com.gce.controller;

import com.gce.service.ITestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author chengen.gu
 * @DATE 2020/2/4 3:22 下午
 */
@RestController
public class DemoController {

    public static Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private ITestService testService;
    @RequestMapping(value = "/test")
    public String helloSentinel(String name){

        long t = System.currentTimeMillis();

        testService.test();

        return testService.hello(t);
    }
}