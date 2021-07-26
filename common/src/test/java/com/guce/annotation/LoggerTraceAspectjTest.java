package com.guce.annotation;

import com.guce.service.LoggerTraceTestService;
import com.guce.service.impl.LoggerTraceTestServiceImpl;
import com.guce.spring.SpringAutoConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author chengen.gce
 * @DATE 2021/7/26 10:17 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringAutoConfiguration.class, LoggerTraceTestServiceImpl.class})
public class LoggerTraceAspectjTest {

    @Autowired
    LoggerTraceTestService loggerTraceTestService ;



    @Test
    public void aroundTest(){
        loggerTraceTestService.test();
    }

}