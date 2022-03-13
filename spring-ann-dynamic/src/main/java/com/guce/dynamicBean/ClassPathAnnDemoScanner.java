package com.guce.dynamicBean;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 * @see https://blog.csdn.net/boling_cavalry/article/details/82083889
 * @Author chengen.gce
 * @DATE 2021/7/18 1:02 下午
 */
@Slf4j
public class ClassPathAnnDemoScanner extends ClassPathBeanDefinitionScanner {

    @Setter
    @Getter
    private Class<? extends Annotation> annotationClass;


    public ClassPathAnnDemoScanner(BeanDefinitionRegistry registry,boolean useDefaultFilters) {
        super(registry,useDefaultFilters);
        //super.addIncludeFilter(new AnnotationTypeFilter(AnnDemo.class));
    }

    public void registerFilters() {
        // if specified, use the given annotation and / or marker interface
        if (this.annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
        }
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {

        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            log.warn("No MyBatis mapper was found in '" + Arrays.toString(basePackages)
                    + "' package. Please check your configuration.");
        } /*else {
            processBeanDefinitions(beanDefinitions);
        }*/

        return beanDefinitions;
    }

    /**
     * todo 判断是接口的话是不是也可以注入到BeanFactory里
     * 可以做Spring内容的RPC操作 使用接口直接做
     *
     * 对于只有接口注入的，那么需要做额外的处理：
     * 如：org.mybatis.spring.mapper.ClassPathMapperScanner#doScan(java.lang.String...)
     * @param beanDefinition
     * @return
     */
    /*@Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }*/

}
