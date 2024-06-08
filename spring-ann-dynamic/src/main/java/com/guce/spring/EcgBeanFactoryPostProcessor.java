package com.guce.spring;

import com.guce.service.IDemoService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @Author chengen.gce
 * @DATE 2022/7/8 21:08
 */
@Component
public class EcgBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        String[]  arr = beanFactory.getBeanNamesForType(IDemoService.class);
        System.out.println("EcgBeanFactoryPostProcessor" +arr);
    }
}
