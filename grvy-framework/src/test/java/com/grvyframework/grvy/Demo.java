package com.grvyframework.grvy;

import com.alibaba.fastjson.JSON;
import com.grvyframework.model.GrvyRuleExecParam;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * @Author chengen.gu
 * @DATE 2020/1/21 9:09 下午
 */
public class Demo {

    public static void main(String[] args) throws IOException {
        GrvyRuleExecParam ruleExecParam = null;
        System.out.println(Optional.ofNullable(ruleExecParam).map(GrvyRuleExecParam::getScript).orElse("script"));

        String script = " def list = [\"1101\",\"1411\",\"1121\",\"1131\"]\n" +
                "    def channels = [\"NET\",\"NCUP\"]\n" +
                "    if (list.contains(交易码) && channels.contains(渠道))\n" +
                "        return true ";
        System.out.println(JSON.toJSONString(script));


        Demo demo = new Demo();
        demo.readFile();
    }

    public void readFile() throws IOException {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("ruleconfigInfo.json");

        String str = IOUtils.toString(in,"UTF-8");
        System.out.println(str);
    }

}
