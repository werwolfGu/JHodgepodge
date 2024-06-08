package com.guce.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ClassUtil;
import com.google.common.collect.ArrayListMultimap;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author chengen.gce
 * @DATE 2024/6/8 19:47
 */
public class Object2Map {

    private final static String SPLIT_POINT = ".";

    /**
     * 将复杂对象扁平化转换成map , 如果出现相同的Key ，存的值是List
     * @param srcObj
     * @return
     */
    public static ArrayListMultimap<String,Object> toFlatMap(Object srcObj) {

        ArrayListMultimap<String,Object> destMap = ArrayListMultimap.create();
        ArrayListMultimap<String,Object> resultMap  = ArrayListMultimap.create();
        Deque<String> stack = new ArrayDeque<>();
        flatMap (srcObj,destMap,stack);

        if (!destMap.isEmpty()) {
            destMap.asMap().entrySet().stream()
                    .forEach( entry -> {
                        Collection collVal = entry.getValue();
                        String key = entry.getKey();
                        if (CollectionUtil.isEmpty(collVal)) {
                            return ;
                        }

                        List list = flatCollecton(collVal);

                        resultMap.put(key, list);

                    });
        }
        return resultMap;

    }

    /**
     * 将对象转换成Map key ：a.b.c value : [aval,bval,cval]
     * @param srcObj
     * @param destMap
     * @param stack
     */
    private static void flatMap(Object srcObj , ArrayListMultimap<String,Object> destMap , Deque<String> stack) {
        Class<?> clazz = srcObj.getClass();
        if (ClassUtil.isSimpleValueType(clazz)) {
            String path = takeAllValue(stack);
            if (StringUtils.isNotEmpty(path)) {
                destMap.put(path,srcObj);
            } else {
                String key = clazz.getCanonicalName();
                destMap.put(key,srcObj);
            }
            return ;
        }

        if (Map.class.isAssignableFrom(clazz)) {
            Map<String,?> srcMap = (Map<String, ?>) srcObj;
            for (Map.Entry<String,?> entry : srcMap.entrySet()) {
                Object val = entry.getValue();
                String key = entry.getKey();
                if (Objects.isNull(val)) {
                    continue;
                }

                stack.addLast(key);
                flatMap(val,destMap,stack);
                stack.removeLast();
            }
        } else if (Collection.class.isAssignableFrom(clazz)) {

            Collection coll = (Collection) srcObj;

            for (Object obj : coll) {
                flatMap(obj , destMap,stack);
            }
        } else if (clazz.isArray()) {
            Object arr[] = (Object[]) srcObj;

            for (Object obj : arr) {
                flatMap(obj,destMap,stack);
            }
        } else {
            Map<String,Object> srcMap = BeanUtil.beanToMap(srcObj,false,true);
            flatMap(srcMap,destMap,stack);
        }

    }


    private static String takeAllValue(Deque<String> stack) {

        if (stack.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String ele : stack) {
            sb.append(SPLIT_POINT).append(ele);
        }
        return sb.substring(1);
    }

    public static <T> List<T> flatCollecton(Collection<T> collVal) {
        List<T> flatList = new ArrayList<>();

        for (T obj : collVal) {
            if (Objects.isNull(obj)) {
                continue;
            }
            if (obj instanceof Collection) {
                flatList.addAll(flatCollecton((Collection<T>) obj));
            } else {
                flatList.add(obj);
            }
        }
        return flatList;
    }
}
