package com.guce.guava;

import com.google.common.base.Splitter;

/**
 * Created by chengen.gu on 2019/9/29.
 */
public class GauvaDemo {

    public static void main(String[] args) {
        Splitter.on(",").split("a,b,c,d").forEach(s -> System.out.println(s));
    }
}
