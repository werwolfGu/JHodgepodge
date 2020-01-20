package com.grvyframework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author guchengen495
 * @date 2020-01-20 16:42
 * @description
 */
@ConfigurationProperties(prefix = "grvy.executor")
@Component("grvyExecutorConfig")
//@PropertySource(value = "classpath:application.properties",encoding = "UTF-8")
public class GrvyExecutorConfig extends ExecutorConfig {
}
