package com.guce.annotation;

import com.guce.common.utils.IdMaker64;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * The type Logger trace aspectj.
 *
 * @Author chengen.gce
 * @DATE 2021 /7/26 10:07 下午
 */
@Slf4j
@Component
@Aspect
public class LoggerTraceAspectj {

    private IdMaker64 idMaker64 = new IdMaker64();
    private final String THREADNO = "T";

    /**
     * Point cut.
     */
    @Pointcut("@annotation(com.guce.annotation.LoggerTrace) || @within(com.guce.annotation.LoggerTrace)")
    public void pointCut() {
    }

    /**
     * Around object.
     *
     * @param joinPoint the join point
     * @return the object
     * @throws Throwable the throwable
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj = null;
        try {
            String threadId = idMaker64.getID();
            System.out.println();

            String methodName = joinPoint.getSignature().getName();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Class<?> classTarget = joinPoint.getTarget().getClass();
            Class<?>[] paraTypes = methodSignature.getParameterTypes();
            Method objMethod = classTarget.getMethod(methodName, paraTypes);

            LoggerTrace switchCache = objMethod.getAnnotation(LoggerTrace.class);
            System.out.println(switchCache);

            obj = joinPoint.proceed();
            MDC.put(THREADNO, threadId);
        } catch (Exception e) {
            log.error("LoggerTraceAspectj 执行异常: ", e);
        } finally {
            MDC.clear();
        }
        return obj;
    }
    
}
