package com.guce.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author chengen.gce
 * @DATE 2021/7/26 10:27 下午
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ThreadPoolClient {

    int corePoolSize() default 0;
    int maximumPoolSize() default 0;

    /**
     * 单位 second
     * @return
     */
    long keepAliveTime() default 0;

    int queueCapacity() default 0;

    String threadNamePrefix() default "";

    Class<?> rejectedExecutionHandler() default ThreadPoolExecutor.AbortPolicy.class;

    boolean useTtl() default true;
}
