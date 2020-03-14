package com.guce.utils;

/**
 * @Author chengen.gce
 * @DATE 2020/3/12 10:13 下午
 */
public class ClassUtils {

    public static ClassLoader getClassLoder(Class<?> klass) {
        ClassLoader loader = null;
        try {
            loader = Thread.currentThread().getContextClassLoader();
        }catch (Throwable ignored){

        }
        if ( loader == null){
            loader = klass.getClassLoader();
            try{
                loader = ClassLoader.getSystemClassLoader();
            }catch (Throwable ignored){

            }
        }
        return loader;
    }
}
