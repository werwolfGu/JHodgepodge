package com.grvyframework.grvy;

import com.grvyframework.grvy.serivce.DemoService;
import com.grvyframework.spring.container.SpringApplicationBean;

/**
 * @Author chengen.gu
 * @DATE 2020/2/18 7:39 下午
 */
public class GrvyInvokeInterface {

    public static String test(String name){
        DemoService demoService = SpringApplicationBean.getBean(DemoService.class);
        System.out.println(demoService.test(name));
        return "invoke spring:" + name;
    }

}
