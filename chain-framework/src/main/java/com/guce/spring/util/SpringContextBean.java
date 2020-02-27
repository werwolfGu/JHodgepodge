package com.guce.spring.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author chengen.gu
 * @DATE 2020/2/13 2:51 下午
 */
@Component
public class SpringContextBean implements ApplicationContextAware {

    private static volatile ApplicationContext ctx ;
    @Override
    public void setApplicationContext( ApplicationContext applicationContext) throws BeansException {

        ctx = applicationContext;
    }

    public static String[] getBeanNamesForType(Class<?> clazz){
        return ctx.getBeanNamesForType(clazz);
    }

    public static <T> T getBean(String beanName){
        return (T) ctx.getBean(beanName);
    }

    public static <T> T getBean (Class<T> beanClass){
        return ctx.getBean(beanClass);
    }
}
