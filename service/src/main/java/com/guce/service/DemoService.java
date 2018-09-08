package com.guce.service;

import com.guce.aop.MyAnnTest;
import com.guce.dao.DemoDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("demoService")
public class DemoService {

    private static Logger logger = LoggerFactory.getLogger(DemoService.class);
    
    @Autowired
    private DemoDao demoDao;

    @MyAnnTest(name = "ann")
    public String helloService(@MyAnnTest(name = "name")  String name,@MyAnnTest(name = "age") String age){
        demoDao.getInfo(new HashMap<>());
        return "hello world";
    }

}
