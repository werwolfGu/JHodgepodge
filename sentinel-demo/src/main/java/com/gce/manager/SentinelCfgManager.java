package com.gce.manager;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gce.manager.model.SentinelCfgModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Author chengen.gu
 * @DATE 2020/2/5 11:53 上午
 */
@Component
public class SentinelCfgManager implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(SentinelCfgManager.class);

    private static String CHARSET = "UTF-8";

    private static String DEFAULT_SENTINEL_CFG = "./cfg/sentinel-config.json";

    @Override
    public void afterPropertiesSet() throws IOException {

        InputStream in = SentinelCfgManager.class.getClassLoader().getResourceAsStream(DEFAULT_SENTINEL_CFG);
        String source = null;

        if (in == null){
            String path = System.getProperty("SENTINEL.APP.CONFIG");
            in = SentinelCfgManager.class.getClassLoader().getResourceAsStream(path);
        }

        if (in != null){
            source = IOUtils.toString(in,CHARSET);
        }


        SentinelCfgModel model  = null;
        if (StringUtils.isNoneBlank(source)){

            model  = JSON.parseObject(source, new TypeReference<SentinelCfgModel>() {});
        }


        if (model != null){
            initSentinelRule(model);

        }
    }

    private void initSentinelRule(SentinelCfgModel sentinelCfg) {
        List<FlowRule> flowRules = sentinelCfg.getFlowRules();
        if (CollectionUtils.isNotEmpty(flowRules)){
            FlowRuleManager.loadRules(flowRules);
        }

        List<DegradeRule> degradeRules = sentinelCfg.getDegradeRules();
        if (CollectionUtils.isNotEmpty(degradeRules)){
            DegradeRuleManager.loadRules(degradeRules);
        }
    }
}
