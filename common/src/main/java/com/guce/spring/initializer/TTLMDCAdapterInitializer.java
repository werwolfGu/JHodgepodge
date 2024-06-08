package com.guce.spring.initializer;

import org.slf4j.TTLLogbackMDCAdapter;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Author chengen.gce
 * @DATE 2024/6/8 20:33
 */
public class TTLMDCAdapterInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        //加载TtlMDCAdapter实例
        TTLLogbackMDCAdapter.getInstance();
    }
}
