package com.grvyframework.param;

import cn.hutool.core.bean.BeanPath;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.grvyframework.model.RuleParamFieldAlias;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author chengen.gce
 * @DATE 2022/4/3 10:47 AM
 */
@Slf4j
public class BeanToMapConver {


    private final static String PREFIX ="[";
    private final static String SUFFIX ="]";
    private final static String DELIMITER =",";
    private final static String EMPTY ="";

    /**
     * 对象属性值映射到Map中
     * 将java对象：指定路径下的属性值映射到Map中，
     * 正常情况下的路径： srcObj b.c[id,name,sex,age]  表示 srcObj 对象下的 b 对应对象的c下的field : id name sex age
     * @param srcObj
     * @param beanPaths
     * @return
     */
    public static Map<String,Object> conver(Object srcObj, List<RuleParamFieldAlias> beanPaths) {

        if (CollectionUtils.isEmpty(beanPaths)){
            return Maps.newHashMap();
        }
        ////对象属性归类
        Map<String,List<RuleParamFieldAlias>> beanPathsMap = beanPathCategorize(beanPaths);

        Map<String,Object> resultMap = new HashMap<>(16);

        beanPathsMap.forEach((key, fieldAliasList) -> {
            //////单点值 如 "id"
            if (CollectionUtils.isEmpty(fieldAliasList)) {
                List<String> arr = Splitter.on("|").splitToList(key);
                BeanPath pattern = BeanPath.create(arr.get(0));
                Object value = pattern.get(srcObj);
                value = Optional.ofNullable(value).orElse(EMPTY);
                if (arr.size() == 1){
                    key = arr.get(0);
                } else {
                    key =arr.get(1);
                }
                resultMap.put(key, value);
                return;
            }

            String beanPath = fieldAliasList.stream()
                    .map(RuleParamFieldAlias::getFieldName)
                    .collect(Collectors.joining(DELIMITER, key + PREFIX,SUFFIX));

            BeanPath pattern = BeanPath.create(beanPath);
            Object obj = pattern.get(srcObj);

            if (obj == null) {
                fieldAliasList.forEach(fieldAlias -> resultMap.put(fieldAlias.getParamName(), EMPTY));
            }

            if (obj instanceof Map) {
                ////// a.b[name,sex,abc]
                Map objMap = (Map) obj;
                fieldAliasList.forEach(fieldAlias -> {

                    String name = fieldAlias.getFieldName();
                    if (!objMap.containsKey(name)) {
                        resultMap.put(fieldAlias.getParamName(), EMPTY);
                    } else {
                        resultMap.put(fieldAlias.getParamName(), objMap.get(name));
                    }
                });
            } else {
                /////a.b[name]
                resultMap.put(fieldAliasList.get(0).getParamName(), obj);
            }

        });

        return resultMap;
    }

    /**
     * 对象路径归类  相同路径下属性归为一类
     * @param beanPaths
     * @return
     */
    public static Map<String,List<RuleParamFieldAlias> > beanPathCategorize(List<RuleParamFieldAlias> beanPaths){

        Map<String,List<RuleParamFieldAlias>> beanPathsMap = new HashMap<>(16);
        ////截取后面的那个 .  Map key
        beanPaths.forEach(fieldAlias -> {

            String path = fieldAlias.getBeanPath();

            int index = StringUtils.lastIndexOf(path,".");


            if (index == -1){
                /////对于 id 单点值路劲的计算
                path = path + "|" + fieldAlias.getAlias();
                beanPathsMap.put(path, null);
            } else {

                String key = path.substring(0,index);
                List<RuleParamFieldAlias> paramList = beanPathsMap.computeIfAbsent(key, k -> Lists.newArrayList());
                if (index < path.length()){

                    String value = path.substring(index + 1);
                    fieldAlias.setFieldName(value);
                    paramList.add(fieldAlias);
                }

            }

        });
        return beanPathsMap;
    }

}
