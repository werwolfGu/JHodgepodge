package com.grvyframework.config;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @author chengen.gu
 * @date 2020-01-20 16:16
 * @description
 */
@Data
public class ExecutorConfig {

    private int corePoolSize = 0;
    private int maximumPoolSize = 0;
    private long keepAliveTime = 0L;

    private TimeUnit unit;

    private int queueSize = 0;

    private String threadName;
}
