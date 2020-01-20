package com.grvyframework.spring.autoconfigure;

import com.grvyframework.config.ExecutorConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author guchengen495
 * @date 2020-01-20 15:01
 * @description
 */
@ComponentScan(basePackages = "com.grvyframework")
@Configuration
@ConfigurationProperties(prefix="grvy.pool.config")
public class GrvyAutoConfiguration {

    @Bean("grvyExecutorConfig")
    public ExecutorConfig executorConfig(){
        ExecutorConfig config = new ExecutorConfig();
        return config;
    }
}
