package com.gce.controller;

import com.alibaba.csp.sentinel.context.ContextUtil;
import com.gce.service.ITestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author chengen.gu
 * @DATE 2020/2/4 3:22 下午
 */
@RestController
public class DemoController {

    public static Logger logger = LoggerFactory.getLogger(DemoController.class);

    private static AtomicInteger count = new AtomicInteger(0);
    @Autowired
    private ITestService testService;
    @RequestMapping(value = "/test")
    public String helloSentinel(String origin){

        //统计 origin
        ContextUtil.enter("test",origin);

        long t = System.currentTimeMillis();
        try{

            if (count.getAndIncrement() %   6 == 1 ){
               t = -1;
            }
            testService.test(origin);
        }catch (Exception e){

        }finally {
           ContextUtil.exit();
        }

        return testService.hello(t);
    }
}
