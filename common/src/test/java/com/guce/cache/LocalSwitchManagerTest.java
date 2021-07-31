package com.guce.cache;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2021/7/28 9:39 下午
 */
class LocalSwitchManagerTest {

    @Test
    void invoke() {
        LocalSwitchManager<String> localSwitchManager = new LocalSwitchManager();
        List<String> list;

        for (int i = 0 ; i < 100 ;i++ ){

            list = localSwitchManager
                    .getlocalSwitchValue("key" , k -> {
                        List<String> result = new ArrayList<>();
                        result.add("abc");
                        result.add("def");
                        System.out.println("==========");
                        return result;
                    });
            System.out.println(list);
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}