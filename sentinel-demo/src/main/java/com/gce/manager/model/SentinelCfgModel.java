package com.gce.manager.model;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * @Author chengen.gu
 * @DATE 2020/2/5 12:01 下午
 */
public class SentinelCfgModel {

    private List<DegradeRule> degradeRules;

    private List<FlowRule> flowRules;

    public List<DegradeRule> getDegradeRules() {
        return degradeRules;
    }

    public void setDegradeRules(List<DegradeRule> degradeRules) {
        this.degradeRules = degradeRules;
    }

    public List<FlowRule> getFlowRules() {
        return flowRules;
    }

    public void setFlowRules(List<FlowRule> flowRules) {
        this.flowRules = flowRules;
    }

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }
}
