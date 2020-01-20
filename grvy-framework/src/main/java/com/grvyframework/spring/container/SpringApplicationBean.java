package com.grvyframework.spring.container;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author chengen.gu
 * @date 2020-01-20 15:07
 * @description
 */
@Component
public class SpringApplicationBean implements ApplicationContextAware {

    private static ApplicationContext ctx ;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    public static <T> T getBean(String beanName){
        return (T) ctx.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz){

        return ctx.getBean(clazz);
    }
}
