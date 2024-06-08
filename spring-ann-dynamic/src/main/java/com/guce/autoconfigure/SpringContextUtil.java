package com.guce.autoconfigure;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author chengen.gce
 * @DATE 2022/7/31 00:06
 */
@Component
public class SpringContextUtil implements ApplicationContextAware , InitializingBean {

    private ApplicationContext applicationContext ;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("aware");
    }
}
