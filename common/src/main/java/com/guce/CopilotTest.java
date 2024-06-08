package com.guce;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author chengen.gce
 * @DATE 2023/3/13 20:28
 */
public class CopilotTest {


    public static void main(String[] args) {

        System.out.println(System.identityHashCode("hello world"));
        ConcurrentMap<String,Object> map = new ConcurrentHashMap<>();
        map.computeIfAbsent("key",k -> new Object());

    }
}
