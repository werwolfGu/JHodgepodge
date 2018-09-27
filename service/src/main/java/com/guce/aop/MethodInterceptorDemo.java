package com.guce.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by chengen.gu on 2018/9/27.
 */
@Component
public class MethodInterceptorDemo implements MethodInterceptor {

    private static Logger logger = LoggerFactory.getLogger(MethodInterceptorDemo.class);
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        logger.warn("spring method Interceptor....");
        return methodInvocation.proceed();
    }
}
