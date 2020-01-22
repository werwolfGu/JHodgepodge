package com.grvyframework.grvy;

import com.grvyframework.model.GrvyRuleExecParam;

import java.util.Optional;

/**
 * @Author chengen.gu
 * @DATE 2020/1/21 9:09 下午
 */
public class Demo {

    public static void main(String[] args) {
        GrvyRuleExecParam ruleExecParam = null;
        System.out.println(Optional.of(ruleExecParam).map(GrvyRuleExecParam::getScript).orElse("script"));
    }
}
