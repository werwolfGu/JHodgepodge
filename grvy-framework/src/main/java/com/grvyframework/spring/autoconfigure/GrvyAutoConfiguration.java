package com.grvyframework.spring.autoconfigure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author chengen.gu
 * @date 2020-01-20 15:01
 * @description
 */
@ComponentScan(basePackages = "com.grvyframework")
@Configuration
@PropertySource(value = {"classpath:grvy-config.properties"})
public class GrvyAutoConfiguration {

}
