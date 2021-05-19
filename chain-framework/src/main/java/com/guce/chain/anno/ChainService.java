package com.guce.chain.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author chengen.gu
 * @DATE 2020/2/13 2:41 下午
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ChainService {

    /**
     * 资源名称
     * @return
     */
    String value()  default "";

    /**
     * 成功执行子流程
     *
     * @return
     */
    String successSubResourceName() default "";

    /**
     * 异常子流程
     *
     * @return
     */
    String exceptionSubResourceName() default "";

    /**
     * service 执行顺序   order 越小  执行优先级越高
     * @return
     */
    int order() default 0;

    /**
     * 是否异步执行
     * @return
     */
    boolean isAsync() default false;

    /**
     * 异步超时时间 配合 isAsync 使用  责任链Service中 取最大值
     * @return
     */
    long asyncTimeout() default 1000;

    /**
     * 流程节点是否为必须节点；默认为必须节点；
     * @return
     */
    boolean isNeedNode() default true;
}
