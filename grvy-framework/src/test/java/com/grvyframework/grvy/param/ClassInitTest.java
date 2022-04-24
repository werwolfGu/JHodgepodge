package com.grvyframework.grvy.param;

import com.grvyframework.param.ClassInit;
import org.junit.Test;

/**
 * @Author chengen.gce
 * @DATE 2022/4/24 21:35
 */
public class ClassInitTest {

    @Test
    public void initInstanceTest(){
        System.out.println(ClassInit.initInstance("int","1"));
        System.out.println(ClassInit.initInstance("long","1"));
        System.out.println(ClassInit.initInstance("double","1"));
        System.out.println(ClassInit.initInstance("boolean","true"));
    }
}
