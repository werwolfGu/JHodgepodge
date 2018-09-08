package com.guce.config;

import com.guce.service.ZkServiceDemo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ZkConfig.class)
public class JavaBeanConfig {

    @Bean("zkServiceDemo")
    public ZkServiceDemo zkServiceDemo(){
        return new ZkServiceDemo();
    }

}
