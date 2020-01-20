package com.grvyframework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author chengen.gu
 * @date 2020-01-20 16:42
 * @description
 */
@ConfigurationProperties(prefix = "grvy.executor")
@Component("grvyExecutorConfig")
public class GrvyExecutorConfig extends ExecutorConfig {
}
