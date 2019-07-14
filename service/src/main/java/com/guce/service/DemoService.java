package com.guce.service;

import com.guce.AppDemo;
import com.guce.aop.MyAnnTest;
import com.guce.cache.GuavaCacheExample;
import com.guce.dao.DemoDao;
import com.guce.example.ExampleDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@Service("demoService")
public class DemoService {

    private static Logger logger = LoggerFactory.getLogger(DemoService.class);

    @Autowired
    private DemoDao demoDao;

    /*@Resource(name = "appDemo")
    private AppDemo AppDemo;*/

//    @Autowired
//    private CacheZkServiceDemo cacheZkServiceDemo;

    @Autowired
    private AppDemo appDemo;

    @Autowired
    private ExampleDemo demo;

    @Resource(name = "guavaCacheExample")
    private GuavaCacheExample guavaCacheExample;

    @MyAnnTest(name = "ann")
    public String helloService(@MyAnnTest(name = "name")  String name,@MyAnnTest(name = "age") String age) throws ExecutionException {
        demoDao.getInfo(new HashMap<>());
        String value = guavaCacheExample.get("key");
//        cacheZkServiceDemo.getZkNodePath();
        appDemo.doSomething();
        demo.doSomething();
        logger.info("guava cache value:{}",value );
        return "hello world";
    }

}
