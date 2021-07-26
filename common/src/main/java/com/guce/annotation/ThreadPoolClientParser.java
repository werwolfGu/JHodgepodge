package com.guce.annotation;

import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author chengen.gce
 * @DATE 2021/7/26 10:35 下午
 */
@Slf4j
@Component("threadPoolClientParser")
public class ThreadPoolClientParser implements BeanPostProcessor {

    private static AtomicInteger threadNumber = new AtomicInteger(1);

    private final String THREAD_NAME_PREFIX = "COMM-" ;

    @Override
    public  Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Class klass = bean.getClass();
        Object targetBean = bean;
        if (AopUtils.isAopProxy(bean)){
            klass = AopUtils.getTargetClass(bean);
            targetBean = AopProxyUtils.getSingletonTarget(bean);
        }

        Field[] fields = klass.getDeclaredFields();

        if (fields == null || fields.length == 0){
            return bean;
        }


        for (Field field : fields) {

            ThreadPoolClient annoClient = field.getAnnotation(ThreadPoolClient.class);
            if (annoClient == null ){
                continue;
            }

            ReflectionUtils.makeAccessible(field);
            ExecutorService executorService = builderThreadPoolExecutor(annoClient);
            try {

                field.set(targetBean,executorService);
            } catch (IllegalAccessException e) {
                log.error("accessException " ,e);
            }

        }
        return bean;
    }

    public ExecutorService builderThreadPoolExecutor(ThreadPoolClient anno){
        int capacity = Optional.ofNullable(anno.queueCapacity())
                .map(size -> size <= 0 ? null : size )
                .orElse(Integer.MAX_VALUE);

        int coreCpu = Optional.ofNullable(anno.corePoolSize())
                .map(size -> size <= 0 ? null : size )
                .orElseGet( () -> Runtime.getRuntime().availableProcessors() + 1);

        int maximumPoolSize = Optional.ofNullable(anno.maximumPoolSize())
                .map(size -> size <= 0 ? null : size )
                .orElseGet( () -> coreCpu * 2 );

        long keepAliveTime = Optional.ofNullable(anno.keepAliveTime())
                .map(size -> size <= 0 ? null : size )
                .orElse(180L);

        String threadNamePrefix = Optional.ofNullable(anno.threadNamePrefix())
                .orElse(THREAD_NAME_PREFIX);

        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        try {
            Class<?> clazz = anno.rejectedExecutionHandler();
            handler = (RejectedExecutionHandler) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("rejected executor exception",e);
        } catch (Exception e) {
            log.error("rejected executor exception",e);
        }

        ExecutorService executorService = new ThreadPoolExecutor(coreCpu, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES, new LinkedBlockingQueue<>(capacity)
                , r -> {

            String name = threadNamePrefix + threadNumber.getAndIncrement();
            Thread t = new Thread(r, name);
            if (t.isDaemon()){
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY){
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        },handler);

        boolean useTtl = anno.useTtl();

        return  useTtl ? TtlExecutors.getTtlExecutorService(executorService) : executorService ;
    }
}
