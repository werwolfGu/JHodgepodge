package com.guce.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * @Author chengen.gu
 * @DATE 2019/11/6 10:13 下午
 */
@Component
@Aspect
public class SysLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(SysLogAspect.class);

    /**
     * @annutation  @within 的区别
     * @annutation 是注解指定方法实现切面编程
     * @within 是注解指定类下所有方法的切面编程；
     *
     */
    @Pointcut("@annotation(com.guce.annotation.LogAnnotation) || @within(com.guce.annotation.LogAnnotation)")
    public void logPointCut(){}

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        MDC.put("key", "value");
        long startTime = System.currentTimeMillis();
        Object result = point.proceed();

        logger.error("aspect cost time:{}",System.currentTimeMillis() - startTime);
        return result ;
    }
}
