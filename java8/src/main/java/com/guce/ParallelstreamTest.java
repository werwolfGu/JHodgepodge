package com.guce;

import java.util.stream.IntStream;

/**
 * @Author chengen.gce
 * @DATE 2021/4/28 1:40 下午
 */
public class ParallelstreamTest {


    public static void main(String[] args) {

        int nums = 100,nums2 = 100;
        IntStream.range(0, nums).parallel().forEach((x) -> {
            IntStream.range(0, nums2).parallel().forEach((y) -> {
                IntStream.range(0, 20).parallel().forEach((z) -> {
                    try {
                        System.out.println(Thread.currentThread().getName() + " " + x + y + z);

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                });
            });
        });
    }
}
