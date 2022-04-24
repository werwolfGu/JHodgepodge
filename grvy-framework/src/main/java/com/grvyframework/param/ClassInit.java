package com.grvyframework.param;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @Author chengen.gce
 * @DATE 2022/4/24 17:25
 */
@Slf4j
public class ClassInit {

    /**
     * 初始化实例对象，通过传过来的类型以及类型值.
     * <p> 当前执行的类型
     * <pre>
     *     int
     *     long
     *     boolean
     *     float
     *     double
     *     BigDecimal
     *     String
     * </pre>
     * <p>默认返回 String类型的
     * @param classType
     * @param defaultValue
     * @return
     */
    public static Object initInstance(String classType,String defaultValue){

        Class<?> clazz = string2Class(classType);
        Object instance = typeMatchCover(clazz,defaultValue);
        return instance;
    }
    /**
     * 对象实例化
     * @param matchType
     * @param matchValue
     * @return
     */
    private static Object typeMatchCover(Class<?> matchType, String matchValue) {

        if (matchType.isAssignableFrom(Boolean.class)) {
            return Boolean.parseBoolean(matchValue);
        }
        if (matchType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(matchValue);
        }
        if (matchType.isAssignableFrom(Long.class)) {
            return Long.valueOf(matchValue);
        }
        if (matchType.isAssignableFrom(Float.class)){
            return Float.valueOf(matchValue);
        }

        if (matchType.isAssignableFrom(Double.class)){
            return Double.valueOf(matchValue);
        }
        if (matchType.isAssignableFrom(BigDecimal.class)){
            return new BigDecimal(matchValue);
        }
        return matchValue;
    }

    /**
     * 明确是哪个类型
     * @param clazzType
     * @return
     */
    private static Class<?> string2Class (String clazzType) {
        if (isBaseType(clazzType)){
            switch (clazzType){
                case "int" :
                    return Integer.class;
                case "long" :
                    return Long.class;
                case "boolean" :
                    return Boolean.class;
                case "float" :
                    return Float.class;
                case "double" :
                    return Double.class;

                default:
                    log.warn("未知基础类型 ： {}" , clazzType);
            }
        }
        Class<?> clazz = null;
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader != null) {
                clazz = loader.loadClass(clazzType);
            } else {
                clazz = ClassLoader.getSystemClassLoader().loadClass(clazzType);
            }

        } catch (ClassNotFoundException e) {
            log.error("类型错误-->: {}" , clazzType,e);
        }
        return clazz;
    }

    /**
     * 判断类型是否为基础类型
     * @param TYPE
     * @return
     */
    private static  boolean isBaseType (String TYPE){

        switch (TYPE){
            case "int" :
            case "long" :
            case "boolean" :
            case "float" :
            case "double" :
                return true;
            default:
                return false;
        }
    }
    public static void main(String[] args) {
        Object obj = initInstance("boolean","true");
        System.out.println(obj);
    }
}
