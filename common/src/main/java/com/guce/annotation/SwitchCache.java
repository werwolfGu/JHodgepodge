package com.guce.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 通过该注解实现的开关，线程隔离，在使用注解方法上对开关赋值，执行方法时，开关值不会发生改变；使用的是ThreadLocal
 *
 * @Author chengen.gce
 * @DATE 2021/8/12 9:27 下午
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SwitchCache {

    /**
     * 开关
     *
     * @return
     */
    String[] keys() default "";

    /**
     * 开关对应的值类型
     *
     * @return
     */
    Class<?>[] valueTypes() default String.class;

    /**
     * 开关对应的描述
     *
     * @return
     */
    String[] keysDesc() default "";

    /**
     * 开关值集合分隔符  正则表达式
     *
     * @return
     */
    String separate() default "";

}
