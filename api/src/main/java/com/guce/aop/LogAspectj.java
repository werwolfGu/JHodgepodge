package com.guce.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author chengen.gu
 * @DATE 2020/1/14 8:12 下午
 */
@Aspect
@Component
public class LogAspectj {

    private static Logger logger = LoggerFactory.getLogger(LogAspectj.class);


    @Pointcut("@annotation(com.guce.aop.PrintLog) || @within(com.guce.aop.PrintLog)")
    public void pointCut(){}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        Object obj = joinPoint.proceed();
        return obj;
    }
}
