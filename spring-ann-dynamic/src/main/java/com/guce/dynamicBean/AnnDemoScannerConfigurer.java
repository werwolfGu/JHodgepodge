package com.guce.dynamicBean;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;

/**
 * @Author chengen.gce
 * @DATE 2021/7/18 12:57 下午
 */
@Slf4j
public class AnnDemoScannerConfigurer  implements BeanDefinitionRegistryPostProcessor {

    @Getter
    @Setter
    private boolean processPropertyPlaceHolders;
    @Setter
    @Getter
    public String basePackage ;
    @Setter
    @Getter
    private Class<? extends Annotation> annotationClass;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {


        ClassPathAnnDemoScanner scanner = new ClassPathAnnDemoScanner(registry,false);

        scanner.setAnnotationClass(this.annotationClass);
        scanner.registerFilters();
        scanner.scan(
                StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
