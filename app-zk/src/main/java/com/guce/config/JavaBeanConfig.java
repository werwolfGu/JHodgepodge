package com.guce.config;

import com.guce.service.CacheZkServiceDemo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ZkConfig.class)
public class JavaBeanConfig {

    @Bean
    public CacheZkServiceDemo cacheZkServiceDemo(){
        return new CacheZkServiceDemo();
    }
}
