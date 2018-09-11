package com.guce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ZkConfig.class)
public class JavaBeanConfig {

}
