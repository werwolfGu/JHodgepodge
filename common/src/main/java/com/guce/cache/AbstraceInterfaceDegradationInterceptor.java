package com.guce.cache;

import com.google.common.collect.Lists;
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

import java.util.List;
import java.util.regex.Pattern;

/**
 * 实现开关统一管理，通过注解 {@link SwitchCache}就能实现开关功能;
 * <p>
 * demo 请看
 * com.guce.interceptor.SwitchCacheInterceptor
 * 注解使用 @SwitchCache
 * com.guce.service.impl.LoggerTraceTestServiceImpl#test()
 *
 * @Author chengen.gce
 * @DATE 2021/8/12 9:37 下午
 */
@Aspect
@Slf4j
public abstract class AbstraceInterfaceDegradationInterceptor {


    private final static Pattern PATTERN = Pattern.compile("[\\,\\;\\|]");

    /**
     * @see SwitchCache  切入点
     */
    @Pointcut("@annotation(com.guce.annotation.InterfaceDegradation) || @within(com.guce.annotation.InterfaceDegradation)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        try {

            Signature signature = joinPoint.getSignature();
            if (!(signature instanceof MethodSignature)) {
                log.warn("InterfaceDegradation 注解只能作用于方法上 ");
                result = joinPoint.proceed();
                return result;
            }
            String methodName = joinPoint.getSignature().getName();

            /*
            MethodSignature methodSignature = (MethodSignature) signature;
            Class<?> classTarget = joinPoint.getTarget().getClass();
            Class<?>[] paraTypes = methodSignature.getParameterTypes();
            Method objMethod = classTarget.getMethod(methodName, paraTypes);

            InterfaceDegradation switchCache = objMethod.getAnnotation(InterfaceDegradation.class);
*/
            List<SwitchConfigInfo> list = loaderValue(methodName);
            if (CollectionUtils.isNotEmpty(list)){

                SwitchConfigInfo configInfo = list.get(0);
                String switchVal = configInfo.getSwitchVal();
                if (StringUtils.equalsIgnoreCase("true",switchVal)){
                    log.warn("method name : {} 降级了，无法访问 " ,methodName );
                    throw new Exception("接口降级");
                }
            }

            result = joinPoint.proceed();
        } catch (Exception e) {
            log.warn("switch cache exception : {} ",e.getMessage(), e);
            throw e;
        }
        return result;
    }


    private List<SwitchConfigInfo> loaderValue(String key) {

        List<SwitchConfigInfo> list = invoke(key);
        if (CollectionUtils.isEmpty(list)) {
            list = Lists.newArrayList();
        }
        return list;
    }

    /**
     * 通过查询DB或redis获取原始值包装成 {@link SwitchConfigInfo} 返回List
     *
     * @param key
     * @return
     */
    public abstract List<SwitchConfigInfo> invoke(String key);

}
