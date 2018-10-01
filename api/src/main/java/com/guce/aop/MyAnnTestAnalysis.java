package com.guce.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

/**
 *
 *实现注解解析类
 * MyAnnTestAnalysis  注解 MyAnnTest 解析类
 */

@Aspect
public class MyAnnTestAnalysis {

    private static Logger logger = LoggerFactory.getLogger(MyAnnTestAnalysis.class);

    @Around("@annotation(com.guce.aop.MyAnnTest)")
//    @Pointcut("execution( * com.guce.*.*(..))")
    public Object methodAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        logger.warn(" methodAround :{}" ,joinPoint);
        return this.invoker(joinPoint);
    }

    public Object invoker(ProceedingJoinPoint joinPoint) throws Throwable {

        if(joinPoint instanceof MethodInvocationProceedingJoinPoint){

            MethodInvocationProceedingJoinPoint methodInvocationProceedingJoinPoint = (MethodInvocationProceedingJoinPoint) joinPoint;

            Field proxy = methodInvocationProceedingJoinPoint.getClass().getDeclaredField("methodInvocation");
            proxy.setAccessible(true);
            ReflectiveMethodInvocation methodInvocation = (ReflectiveMethodInvocation) proxy.get(joinPoint);
            Method method = methodInvocation.getMethod();
            Annotation annotation =  method.getAnnotation(MyAnnTest.class);
            if(annotation != null){

                Parameter[] parameters = method.getParameters();
                if(parameters != null && parameters.length > 0 ){

                    Object[] objects = methodInvocation.getArguments();
                    for(int i = 0 ; i < parameters.length ; i++){
                        Parameter parameter = parameters[i];
                        Annotation myAnn = parameter.getAnnotation(MyAnnTest.class);
                        Object obj = objects[i];
                        if(Objects.isNull(obj) && myAnn != null){
                            MyAnnTest myAnnTest = (MyAnnTest) myAnn;
                            String name = myAnnTest.name();
                            objects[i] = name;
                        }

                    }
                }

            }

        }

        return joinPoint.proceed();
    }
}
