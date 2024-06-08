package com.guce.common;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author chengen.gce
 * @DATE 2023/2/16 20:40
 */
public class BeanToMapUtil {

    public static Object toMap(Object bean) {
        if (Objects.isNull(bean)) {
            return null;
        }
        Class<?> clazz = bean.getClass();
        if (ClassUtil.isSimpleValueType(clazz)) {
            return bean;
        }
        if (clazz.isArray()) {
            Class<?> arrClazz = clazz.getComponentType();
            if (ClassUtil.isSimpleValueType(arrClazz)) {
                return bean;
            }
            int len = Array.getLength(bean);
            List<Object> arrList = new ArrayList<>(len);
            for (int i = 0 ; i < len ; i++) {
                Object obj = Array.get(bean,i);
                arrList.add(toMap(obj));
            }
            return arrList;
        } else if (bean instanceof Collection) {
            Collection collection = (Collection) bean;
            List<Object> list = new ArrayList<>(collection.size());
            for (Object obj : collection) {
                list.add(toMap(obj));
            }
            return list;
        } else if (bean instanceof Map) {
            Map<String,Object> result = new HashMap<>();
            Map<?,?> map = (Map) bean;
            map.entrySet().parallelStream()
                    .filter( entry -> {
                        return !Objects.isNull(entry.getValue());
                    }).forEach(entry -> {
                        result.put(entry.getKey().toString(),entry.getValue());
                    });
            return result;
        } else if (bean instanceof Enum){
            return ((Enum)bean).name();
        } else {
            return beanToMap(bean);
        }
    }

    public static Map<String,Object> beanToMap(Object bean) {
        if (Objects.isNull(bean)) {
            return null;
        }
        Class<?> clazz = bean.getClass();
        Field[] fields = ReflectUtil.getFields(clazz);

        if (fields == null || fields.length <= 0) {
            return null;
        }
        Map<String,Object> dataMap = new HashMap<>();

        for (Field field : fields) {
            Object obj = ReflectUtil.getFieldValue(bean,field);
            String fieldName = ReflectUtil.getFieldName(field);
            dataMap.put(fieldName,toMap(obj));
        }
        return dataMap;
    }

    public static void main(String[] args) {
        Object[] objects = new Integer[]{1,2,3,4};
        for (Object obj : objects) {
            System.out.println(BeanUtil.getBeanDesc(obj.getClass()).getName());
            System.out.println(obj);
        }

    }
}
