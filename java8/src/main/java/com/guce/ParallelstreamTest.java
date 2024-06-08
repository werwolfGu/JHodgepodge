package com.guce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @Author chengen.gce
 * @DATE 2021/4/28 1:40 下午
 */
public class ParallelstreamTest {


    public static void main(String[] args) {
        Map<String, List<String>> map = new HashMap<>();
        map.computeIfAbsent("key", (key) -> new ArrayList<>());
        int nums = 1000, nums2 = 100;
        List<Integer> list = new ArrayList<>();
        List<Integer> list1 = new ArrayList<>();
        for (int i = 0; i < 10000000; i++) {
            list.add(i);
            if (i % 100 == 0) {
                list1.add(i);
            }
        }
        System.out.println("=========================");
        list.parallelStream().forEach(i -> {

            CompletableFuture future = CompletableFuture.runAsync(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " " + i + " -> ");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
