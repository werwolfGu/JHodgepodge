package com.gce.demo.apollo;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.csp.sentinel.datasource.apollo.ApolloDataSource;

import java.util.List;

/**
 * @Author chengen.gu
 * @DATE 2020/2/5 11:47 上午
 */
public class ApolloDataSourceDemo {

    private static final String KEY = "TestResource";

    public static void main(String[] args) {
        loadRules();
    }

    private static void loadRules() {
        // Set up basic information, only for demo purpose. You may adjust them based on your actual environment.
        // For more information, please refer https://github.com/ctripcorp/apollo
        String appId = "sentinel-demo";
        String apolloMetaServerAddress = "http://localhost:8080";
        System.setProperty("app.id", appId);
        System.setProperty("apollo.meta", apolloMetaServerAddress);

        String namespaceName = "application";
        String flowRuleKey = "flowRules";
        // It's better to provide a meaningful default value.
        String defaultFlowRules = "[]";

        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ApolloDataSource<>(namespaceName,
                flowRuleKey, defaultFlowRules, source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
        }));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }
}
