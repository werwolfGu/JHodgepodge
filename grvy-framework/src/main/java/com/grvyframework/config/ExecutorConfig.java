package com.grvyframework.config;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @author guchengen495
 * @date 2020-01-20 16:16
 * @description
 */
@Data
public class ExecutorConfig {

    private int corePoolSize = 10;
    private int maximumPoolSize = 20;
    private long keepAliveTime = 3;

    private TimeUnit unit;

    private int queueSize = Integer.MAX_VALUE;
}
