package com.grvyframework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author guchengen495
 * @date 2020-01-20 16:42
 * @description
 */
@ConfigurationProperties(prefix = "grvy.executor")
public class GrvyExecutorConfig extends ExecutorConfig {
}
