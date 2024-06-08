package com.guce.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;

/**
 * @Author chengen.gce
 * @DATE 2022/4/9 7:02 PM
 */
@Component("serviceA")
public class ServiceA implements  InitializingBean {

    @Autowired
    private ServiceB b;
    private ExecutorService executorService;

    public void test(){
        System.out.println("test");
    }

    @PostConstruct
    public void init(){
        System.out.println("init");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }
}
