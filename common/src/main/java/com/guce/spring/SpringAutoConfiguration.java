package com.guce.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @Author chengen.gce
 * @DATE 2021/7/26 10:11 下午
 */
@EnableAspectJAutoProxy
@Configuration
@ComponentScan(basePackages = {"com.guce"})
public class SpringAutoConfiguration {

    /*@Bean("restHighLevelClient")
    public ESClientFactory esClientFactory() {

        ESClientFactory factory = new ESClientFactory();

        factory.setHostsStr("");
        return factory;
    }*/

}
