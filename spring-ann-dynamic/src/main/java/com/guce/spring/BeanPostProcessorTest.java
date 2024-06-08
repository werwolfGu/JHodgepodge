package com.guce.spring;

import com.guce.service.IDemoService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @Author chengen.gce
 * @DATE 2022/4/10 11:54 AM
 */
@Component
public class BeanPostProcessorTest implements BeanPostProcessor {

    @Autowired
    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("serviceA".equals(beanName)){
            System.out.println("before serviceA");
        }
        return bean;
    }
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        IDemoService obj = (IDemoService) beanFactory.getBean("demoService1Impl");
        //obj.helloWorld();
        System.out.println("beanPostProcessor:" + beanName);
        return bean;
    }
}
