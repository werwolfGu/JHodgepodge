package com.guce;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;

/**
 * @Author chengen.gce
 * @DATE 2021/5/10 4:40 下午
 */
public class App {

    public static void main(String[] args) {
        ExpressRunner runner = new ExpressRunner();

        for (int i = 0 ; i < 10 ; i++ ){
            long start = System.currentTimeMillis();
            DefaultContext<String, Object> context = new DefaultContext<String, Object>();
            context.put("a",1);
            context.put("b",2);
            context.put("c",3);
            String express = "a+b*c";
            Object r = null;
            try {
                r = runner.execute(express, context, null, true, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(r + " cost time: " + (System.currentTimeMillis()- start));
        }

    }

    public static void test(){
       // String exp = "如果 (卡号列表包含 cardLogo 且 )"
    }
}
