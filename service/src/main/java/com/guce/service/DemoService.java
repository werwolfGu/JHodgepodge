package com.guce.service;

import com.guce.AppDemo;
import com.guce.aop.MyAnnTest;
import com.guce.cache.GuavaCacheExample;
import com.guce.example.ExampleDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.concurrent.ExecutionException;

@Service("demoService")
public class DemoService {

    private static Logger logger = LoggerFactory.getLogger(DemoService.class);

    /*@Autowired
    private DemoDao demoDao;*/

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
    @Transactional
    public String helloService(@MyAnnTest(name = "name") String name, @MyAnnTest(name = "age") String age) throws ExecutionException {
        // demoDao.getInfo(new HashMap<>());
        String value = guavaCacheExample.get("key");
//        cacheZkServiceDemo.getZkNodePath();
        appDemo.doSomething();
        demo.doSomething();
        logger.info("guava cache value:{}", value);
        return "hello world";
    }

    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
