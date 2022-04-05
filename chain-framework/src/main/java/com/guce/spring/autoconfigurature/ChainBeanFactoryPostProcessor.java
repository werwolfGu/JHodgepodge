package com.guce.spring.autoconfigurature;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @Author chengen.gce
 * @DATE 2021/7/16 10:41 下午
 */
//@Component
@Deprecated
public class ChainBeanFactoryPostProcessor  implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        System.out.println("======>" + beanFactory);
    }
}
