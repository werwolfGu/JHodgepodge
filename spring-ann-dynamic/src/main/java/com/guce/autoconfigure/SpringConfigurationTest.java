package com.guce.autoconfigure;

import com.guce.service.DemoService1Impl;
import com.guce.service.DemoService2Impl;
import com.guce.service.IDemoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author chengen.gce
 * @DATE 2022/8/1 00:03
 */
@Configuration
public class SpringConfigurationTest {

    @Bean("demoServic2Impl")
    public IDemoService demoService2Impl () {
        return new DemoService2Impl();
    }

    @Bean("demoService1Impl")
    public IDemoService demoService1Impl () {
        return new DemoService1Impl();
    }
}
