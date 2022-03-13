package com.guce.cache;

/**
 * @Author chengen.gce
 * @DATE 2021/8/12 9:30 下午
 */
class SwitchCacheServer extends SwitchCacheClient {

    public static void setSwitchValue(String key, Object val) {
        SWITCH_CACHE_THREAD_LOCAL.get().put(key, val);
    }

    public static void remove() {
        SWITCH_CACHE_THREAD_LOCAL.remove();
    }

}
