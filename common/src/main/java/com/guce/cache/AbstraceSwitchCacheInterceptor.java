package com.guce.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.guce.annotation.SwitchCache;
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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 实现开关统一管理，通过注解 {@link SwitchCache}就能实现开关功能;
 *
 * <p>demo 请看 com.guce.interceptor.SwitchCacheInterceptor 注解使用 @SwitchCache
 * com.guce.service.impl.LoggerTraceTestServiceImpl#test() @Author chengen.gce @DATE 2021/8/12 9:37
 * 下午
 */
@Aspect
@Slf4j
public abstract class AbstraceSwitchCacheInterceptor {

    private final LoadingCache<String, List<SwitchConfigInfo>> loadingSwitchCache =
            Caffeine.newBuilder()
                    .expireAfterWrite(3, TimeUnit.HOURS)
                    .refreshAfterWrite(30, TimeUnit.SECONDS)
                    .maximumSize(10000)
                    .build(this::loaderValue);
    private static final Pattern PATTERN = Pattern.compile("[,;|]");

    /**
     * @see com.guce.annotation.SwitchCache 切入点
     */
    @Pointcut(
            "@annotation(com.guce.annotation.SwitchCache) || @within(com.guce.annotation.SwitchCache)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        try {

            Signature signature = joinPoint.getSignature();
            if (!(signature instanceof MethodSignature)) {
                log.warn("switchCache 注解只能作用于方法上 ");
                result = joinPoint.proceed();
                return result;
            }
            String methodName = joinPoint.getSignature().getName();
            MethodSignature methodSignature = (MethodSignature) signature;
            Class<?> classTarget = joinPoint.getTarget().getClass();
            Class<?>[] paraTypes = methodSignature.getParameterTypes();
            Method objMethod = classTarget.getMethod(methodName, paraTypes);

            SwitchCache switchCache = objMethod.getAnnotation(SwitchCache.class);
            wrapperAnnotationValue(switchCache);

            result = joinPoint.proceed();
        } catch (Exception e) {
            log.warn("switch cache exception : ", e);
        } finally {
            SwitchCacheServer.remove();
        }
        return result;
    }

    /**
     * 解析获取开关值 ，解析类型如：@SwitchCache(keys = {"key1", "key2", "key3", "key4" ,"key7:defaultValue"},
     * valueTypes = {String.class, Set.class, List.class})
     *
     * @param switchCache
     */
    private void wrapperAnnotationValue(SwitchCache switchCache) {

        String[] keyArr = switchCache.keys();
        Class[] valTypeArr = switchCache.valueTypes();
        String[] keysDesc = switchCache.keysDesc();

        if (keyArr == null || keyArr.length == 0) {
            return;
        }
        int keyLen = keyArr.length;
        int valLen = 0;
        if (valTypeArr != null || valTypeArr.length != 0) {
            valLen = valTypeArr.length;
        }

        int keyDescLen = 0;
        if (keysDesc != null) {
            keyDescLen = keysDesc.length;
        }

        Date currDate = new Date();
        for (int i = 0; i < keyLen; i++) {
            String switchKey = keyArr[i];
            String[] keyAndDefaultValue = switchKey.split(":");
            String key = switchKey;
            String defaultValue = null;
            if (keyAndDefaultValue.length == 2) {
                key = keyAndDefaultValue[0];
                defaultValue = keyAndDefaultValue[1];
            }
            List<SwitchConfigInfo> list = loadingSwitchCache.get(key);

            Class<?> valType = String.class;
            if (i < valLen) {
                valType = valTypeArr[i];
            }
            String keyDesc = "";
            if (i < keyDescLen) {
                keyDesc = keysDesc[i];
            }
            Class<?> finalValType = valType;
            Pattern finalTmpPattern = PATTERN;
            String finalDefaultValue = defaultValue;
            String finalKey = key;
            Object objVal =
                    Optional.ofNullable(list).map(configInfoList -> configInfoList.stream()
                                            .filter(
                                                    configInfo -> {
                                                        Date startTime = configInfo.getStartTime();
                                                        Date endTime = configInfo.getEndTime();
                                                        String isValid = configInfo.getIsValid();
                                                        if (StringUtils.equals("0", isValid)) {
                                                            return false;
                                                        }
                                                        if (startTime != null && startTime.compareTo(currDate) > 0) {
                                                            return false;
                                                        }

                                                        if (endTime != null && currDate.compareTo(endTime) > 0) {
                                                            return false;
                                                        }
                                                        return true;
                                                    })
                                            .findFirst()
                                            .orElse(null))
                            .map(SwitchConfigInfo::getSwitchVal)
                            .map(
                                    value -> {
                                        try {
                                            return typeMatchCover(finalValType, value, finalTmpPattern);
                                        } catch (Exception e) {
                                            log.warn("switch cache 获取开关值异常 key : {} ; value : {} ; type: {}",
                                                    finalKey,
                                                    value,
                                                    finalValType.getName(),
                                                    e);
                                        }
                                        return null;
                                    })
                            .orElseGet(
                                    () -> {
                                        try {

                                            if (StringUtils.isBlank(finalDefaultValue)) {
                                                return null;
                                            }
                                            return typeMatchCover(finalValType, finalDefaultValue, finalTmpPattern);

                                        } catch (Exception e) {
                                            log.warn("switch cache 获取开关默认值异常 key : {} ; value : {} ; type: {}",
                                                    finalKey,
                                                    finalDefaultValue,
                                                    finalValType.getName(),
                                                    e);
                                        }
                                        return null;
                                    });

            log.info("switch-cache ; desc : {} key : {}  ; value : {} ; type: {}"
                    , keyDesc, key, objVal, finalValType);
            if (!Objects.isNull(objVal)) {
                SwitchCacheServer.setSwitchValue(key, objVal);
            }
        }
    }

    /**
     * 数据类型匹配转换
     *
     * @param matchType
     * @param matchValue
     * @param finalTmpPattern
     * @return
     */
    private Object typeMatchCover(Class<?> matchType, String matchValue, Pattern finalTmpPattern) {

        if (matchType.isAssignableFrom(Boolean.class)) {
            return Boolean.parseBoolean(matchValue);
        }
        if (matchType.isAssignableFrom(List.class)) {
            return Splitter.on(finalTmpPattern).splitToList(matchValue);
        }
        if (matchType.isAssignableFrom(Set.class)) {
            Iterable<String> valList = Splitter.on(finalTmpPattern).split(matchValue);
            return Sets.newHashSet(valList);
        }
        if (matchType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(matchValue);
        }
        if (matchType.isAssignableFrom(Long.class)) {
            return Long.valueOf(matchValue);
        }
        return matchValue;
    }

    private List<SwitchConfigInfo> loaderValue(String key) {

        List<SwitchConfigInfo> list = invoke(key);
        if (CollectionUtils.isEmpty(list)) {
            list = Lists.newArrayList();
        }
        return list;
    }

    /**
     * 通过查询DB或redis获取原始值包装成 {@link com.guce.module.SwitchConfigInfo} 返回List
     *
     * @param key
     * @return
     */
    public abstract List<SwitchConfigInfo> invoke(String key);
}
