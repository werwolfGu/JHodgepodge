package com.guce.cache;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author chengen.gce
 * @DATE 2021/8/12 9:30 下午
 */
public class SwitchCacheClient {

    protected final static ThreadLocal<Map<String, Object>> SWITCH_CACHE_THREAD_LOCAL =
            new TransmittableThreadLocal<Map<String, Object>>() {
                @Override
                public Map<String, Object> initialValue() {
                    return new ConcurrentHashMap<>();
                }
            };

    public static <V> V getSwtichValue(String key) {
        return getSwtichValue(key, null);
    }

    public static <V> V getSwtichValue(String key, V defaultVal) {

        V val = (V) SWITCH_CACHE_THREAD_LOCAL.get().getOrDefault(key, defaultVal);
        return val;
    }
}
