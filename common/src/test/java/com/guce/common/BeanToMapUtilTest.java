package com.guce.common;

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author chengen.gce
 * @DATE 2023/2/16 21:31
 */
public class BeanToMapUtilTest {


    @Test
    public void beanToMap(){
        List<A> list = new ArrayList<>();
        List<B> bList = new ArrayList<>();
        A a = new A();
        a.setId("1");
        a.setBList(bList);
        list.add(a);

        B b = new B();
        b.setAge(18);
        b.setName("gce");
        b.setSex("男");
        bList.add(b);
        b = new B();
        b.setAge(19);
        b.setName("gce1");
        b.setSex("男1");
        bList.add(b);


        bList = new ArrayList<>();
        a = new A();
        a.setId("2");
        a.setBList(bList);
        list.add(a);

         b = new B();
        b.setAge(20);
        b.setName("gce2");
        b.setSex("男2");
        bList.add(b);
        b = new B();
        b.setAge(22);
        b.setName("gce22");
        b.setSex("男22");
        bList.add(b);
        Map<String,Object> map = new HashMap<>();
        map.put("key1","val1");
        map.put("key2","val2");
        map.put("key3",3);
        a.setMapObj(map);
        Map<String,Object> result = new HashMap<>();
        BeanToMapUtil.beanToMap(list,result);
        System.out.println(result);


        result = new HashMap<>();
        BeanToMapUtil.beanToMap(a,result);
        System.out.println(result);
    }

    @Getter
    @Setter
    public static class A {
        Map<String,Object> MapObj ;
        String id ;
        List<B> bList ;
    }

    @Getter
    @Setter
    public static class B {
        private String name;
        private int age;
        private String sex;
    }
}
