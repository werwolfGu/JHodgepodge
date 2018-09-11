package com.guce.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZkConfig {

    @Value("${zk.connect.servers}")
    private String zkConnectStr;

    @Value("${zk.retry.sleep.time:1000}")
    private int baseSleepTime;

    @Value("${zk.max.retries:3}")
    private int maxRetries;

    @Value("${zk.connection.timeout:5000}")
    private int connectionTimeout;

    @Value("${zk.session.timeout:60000}")
    private int sessionTimeout;

    @Bean
    public CuratorFramework zkClient(){

        return CuratorFrameworkFactory.builder().connectString(zkConnectStr)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .connectionTimeoutMs(connectionTimeout)
                .sessionTimeoutMs(sessionTimeout)
                .build();
    }
}
