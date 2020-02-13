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
public @interface ChainSerivce {

    String value()  default "";

    int order() default 0;

    boolean isAsync() default false;
}
