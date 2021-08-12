package com.guce.annotation;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.guce.module.SwitchConfigInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @Author chengen.gce
 * @DATE 2021/8/12 9:37 下午
 */
@Aspect
@Slf4j
public abstract class AbstraceSwitchCacheInterceptor {

    private LoadingCache<String,List<SwitchConfigInfo>> loadingSwitchCache = Caffeine.newBuilder()
            .expireAfterWrite(3, TimeUnit.HOURS)
            .refreshAfterWrite(3,TimeUnit.MINUTES)
            .maximumSize(10000)
            .build(this::loaderValue);
    private Pattern pattern = Pattern.compile("[\\,\\;\\|]");
    /**
     *@see com.guce.annotation.SwitchCache  切入点
     */
    @Pointcut("@annotation(com.guce.annotation.SwitchCache) || @within(com.guce.annotation.SwitchCache)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        try{

            Signature signature = joinPoint.getSignature();
            if (!(signature instanceof MethodSignature)){
                log.warn("switchCache 注解只能作用于方法上 ");
                result = joinPoint.proceed();
                return result;
            }
            String methodName=joinPoint.getSignature().getName();
            MethodSignature methodSignature = (MethodSignature) signature;
            Class<?> classTarget=joinPoint.getTarget().getClass();
            Class<?>[] par=methodSignature.getParameterTypes();
            Method objMethod=classTarget.getMethod(methodName, par);

            SwitchCache switchCache =objMethod.getAnnotation(SwitchCache.class);
            wrapperAnnotationValue(switchCache);

            result = joinPoint.proceed();
        }catch (Exception e){
            log.warn("switch cache exception : ",e);
        }finally {
            SwitchCacheServer.remove();
        }
        return result;
    }

    /**
     * 获取开关值，同时做类型判断
     * @param switchCache
     */
    private void wrapperAnnotationValue(SwitchCache switchCache){

        String[] keyArr = switchCache.keys();
        Class[] valTypeArr = switchCache.valueTypes();

        if (keyArr == null){
            return ;
        }
        int keyLen = keyArr.length;
        int valLen = 0;
        if (valTypeArr != null){
            valLen = valTypeArr.length;
        }

        Date currDate = new Date();
        for (int i = 0 ; i < keyLen ; i++){
            String key = keyArr[i];
            List<SwitchConfigInfo> list = loadingSwitchCache.get(key);
            if (CollectionUtils.isEmpty(list)){
                continue;
            }

            Class<?> valType = String.class;
            if (i < valLen){
                valType = valTypeArr[i];
            }

            Class<?> finalValType = valType;

            Object objVal = list.stream()
                    .filter( configInfo -> {

                        Date startTime = configInfo.getStartTime();
                        Date endTime = configInfo.getEndTime();
                        String isValid = configInfo.getIsValid();
                        if (StringUtils.equals("0",isValid)){
                            return false ;
                        }
                        if (startTime != null && startTime.compareTo(currDate) > 0) {
                            return false;
                        }

                        if (endTime != null && currDate.compareTo(endTime) > 0){
                            return false;
                        }
                        return true;

                    })
                    .findFirst()
                    .map( configInfo -> configInfo.getSwitchVal())
                    .map( value -> {
                        if (finalValType.isAssignableFrom(List.class)){

                            return Splitter.on(pattern).splitToList(value);
                        }
                        if (finalValType.isAssignableFrom(Set.class)) {
                            List<String> valList = Splitter.on(pattern).splitToList(value);
                            return Sets.newHashSet(valList);
                        }
                        if (finalValType.isAssignableFrom(Integer.class)){
                            return Integer.valueOf(value);
                        }
                        if (finalValType.isAssignableFrom(Long.class)){
                            return Long.valueOf(value);
                        }
                        return value;
                    }).orElse(null);

            SwitchCacheServer.setSwitchValue(key,objVal);
        }

    }

    private List<SwitchConfigInfo> loaderValue(String key){

        List<SwitchConfigInfo> list = invoke(key);
        if (CollectionUtils.isEmpty(list)){
            list = Lists.newArrayList();
        }
        return list;
    }

    /**
     *  通过查询DB或redis获取原始值包装成 {@link com.guce.module.SwitchConfigInfo} 返回List
     * @param key
     * @return
     */
    public abstract List<SwitchConfigInfo> invoke(String key);

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("[\\,\\;\\|]");
        String value = "ab,d,d,w,r,t;d|q";
        List<String> list = Splitter.on(pattern).splitToList(value);
        System.out.println(list);
    }
}
