package com.guce.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Author chengen.gce
 * @DATE 2022/4/10 10:49 AM
 */
@Aspect
@Component
public class CacheDependencyAOPTest {

    @Pointcut("execution(* com.guce.service.ServiceA.*(..))")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj = null;
        try {
            System.out.println("aspectj before");
            obj = joinPoint.proceed();
            System.out.println("aspectj after");
        } catch (Exception e) {
            System.out.println(e);
        } finally {

        }
        return obj;
    }

}
