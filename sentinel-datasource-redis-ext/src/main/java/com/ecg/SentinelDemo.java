package com.ecg;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.redisson.api.RedissonClient;

import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2021/11/19 8:52 下午
 */
public class SentinelDemo {

    private static Converter<String, List<FlowRule>> buildFlowConfigParser() {
        return source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
        });
    }

    private static Converter<String, List<DegradeRule>> buildFDegradeConfigParser() {
        return source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {
        });
    }

    public static void main(String[] args) {

        Converter<String, List<FlowRule>> flowConfigParser = buildFlowConfigParser();
        RedissonClient redissonClient = null;
        String ruleKey = "flowRule:${appid}";
        String channel = "flow:topic:${appid}";
        ReadableDataSource<String, List<FlowRule>> redisDataSource = new RedisDataSource<List<FlowRule>>(redissonClient,
                ruleKey, channel, flowConfigParser){
            @Override
            public String defaultReadSrouce() {
                return null;
            }
        };
        FlowRuleManager.register2Property(redisDataSource.getProperty());


        String degradeKey = "degradeRule:${appid}";
        String degradeTopic = "degrade:topic:${appid}";
        ReadableDataSource<String, List<DegradeRule>> redisDegradeRule = new RedisDataSource<List<DegradeRule>>(redissonClient,
                degradeKey, degradeTopic, buildFDegradeConfigParser()){
            @Override
            public String defaultReadSrouce() {
                return null;
            }
        };
        DegradeRuleManager.register2Property(redisDegradeRule.getProperty());
    }
}
