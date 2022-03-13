package com.guce.loadOrder;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author chengen.gce
 * @DATE 2021/11/18 11:32 下午
 */

@Component("springInitLoaderOrder")
public class SpringInitLoaderOrder implements InitializingBean, ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    SpringInitLoaderOrder1 chainServiceDemo1;

    @PostConstruct
    public void init () {
        System.out.println(" SpringInitLoaderOrder PostConstruct      : " + chainServiceDemo1);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(" SpringInitLoaderOrder afterPropertiesSet      : " + chainServiceDemo1);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("SpringInitLoaderOrder  onApplicationEvent     : " + chainServiceDemo1);
    }
}
