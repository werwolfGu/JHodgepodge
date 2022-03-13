package com.guce.autoconfigure;

import com.guce.anntation.AnnDemo;
import com.guce.anntation.AnnDemoScan;
import com.guce.dynamicBean.AnnDemoScannerConfigurer;
import com.guce.dynamicBean.ClassPathAnnDemoScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author chengen.gce
 * @DATE 2021/7/18 12:50 下午
 *
 * https://felord.cn/custom-importBeanDefinitionRegistrar.html
 */
@Slf4j
@Configuration
@ConditionalOnMissingBean({AnnDemoScannerConfigurer.class, ClassPathAnnDemoScanner.class})
public class AutoConfiguredAnnDemoScannerRegistrar  implements ImportBeanDefinitionRegistrar, BeanFactoryAware , ResourceLoaderAware {

    private BeanFactory beanFactory ;

    private ResourceLoader resourceLoader ;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

        this.beanFactory = beanFactory ;

    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 不使用默认过滤器
        /*ClassPathAnnDemoScanner xBeanDefinitionScanner = new ClassPathAnnDemoScanner(registry, false);
        xBeanDefinitionScanner.setResourceLoader(resourceLoader);
        // 扫描XBeanScan注解指定的包
        xBeanDefinitionScanner.scan(getBasePackagesToScan(importingClassMetadata));*/

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(AnnDemoScannerConfigurer.class);
        builder.addPropertyValue("annotationClass", AnnDemo.class);
        String basePackages =getBasePackagesToScan(importingClassMetadata);
        builder.addPropertyValue("basePackage", basePackages);
        BeanWrapper beanWrapper = new BeanWrapperImpl(AnnDemoScannerConfigurer.class);
        Set<String> propertyNames = Stream.of(beanWrapper.getPropertyDescriptors()).map(PropertyDescriptor::getName)
                .collect(Collectors.toSet());
        registry.registerBeanDefinition(AnnDemoScannerConfigurer.class.getName(), builder.getBeanDefinition());
        // AnnDemoScannerConfigurer annDemoScannerConfigurer = new AnnDemoScannerConfigurer();
    }

    private String getBasePackagesToScan(AnnotationMetadata metadata) {
       String name = AnnDemoScan.class.getName();
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(name, true));
        Assert.notNull(attributes, () -> "No auto-configuration attributes found. Is " + metadata.getClassName()
                + " annotated with " + ClassUtils.getShortName(name) + "?");
        String[] basePackages = attributes.getStringArray("basePackages");

        return StringUtils.arrayToDelimitedString(basePackages,",");
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
