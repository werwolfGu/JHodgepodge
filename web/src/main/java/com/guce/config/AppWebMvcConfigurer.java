package com.guce.config;

import com.guce.interceptor.InterceptorAsyncHandler;
import com.guce.interceptor.InterceptorDemo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = "com.guce")
@EnableWebMvc
public class AppWebMvcConfigurer implements WebMvcConfigurer {

    @Bean
    public InterceptorDemo interceptorDemo(){
        return new InterceptorDemo();
    }

    @Bean
    public AsyncHandlerInterceptor interceptorAsyncHandler(){
        return new InterceptorAsyncHandler();
    }

   @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {

       configurer.setDefaultTimeout(1000);   //设置异步超时时间

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // registry.addInterceptor(interceptorDemo()).addPathPatterns("/**");

        registry.addInterceptor(interceptorAsyncHandler()).addPathPatterns("/**").order(1);

    }

}
