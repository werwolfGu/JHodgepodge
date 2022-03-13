package com.guce.loadOrder;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @Author chengen.gce
 * @DATE 2021/11/18 11:32 下午
 */
@Service
public class SpringInitLoaderOrder1 implements InitializingBean, ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    SpringInitLoaderOrder chainServiceDemo1;

    @PostConstruct
    public void init () {
        System.out.println(" SpringInitLoaderOrder1 PostConstruct      : " + chainServiceDemo1);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(" SpringInitLoaderOrder1 afterPropertiesSet      : " + chainServiceDemo1);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println(" SpringInitLoaderOrder1 onApplicationEvent     : " + chainServiceDemo1);
    }
}
