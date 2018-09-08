package com.guce.config;

import com.guce.servlet.filter.FilterDemo;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterDemo filterDemo(){
        return new FilterDemo();
    }

    @Bean
    public FilterRegistrationBean filterDemoRegistration(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(filterDemo());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("paramName", "paramValue");
        filterRegistrationBean.setName("filterDemo3");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }
}
