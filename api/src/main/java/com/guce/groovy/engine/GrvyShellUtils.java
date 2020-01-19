package com.guce.groovy.engine;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.Arrays;
import java.util.List;

public class GrvyShellUtils {



    public static void main(String[] args) {

        List<String> mcc = Arrays.asList("1","2","3");
        Binding binding = new Binding();
        binding.setProperty("卡类型",mcc);
        List<String> cardLogoList = Arrays.asList("123","456");
        GroovyShell grvyShell = new GroovyShell(binding);
        StringBuilder str = new StringBuilder();
        str.append("卡类型.contains(\"2\") ");
        binding.setProperty("卡logo",cardLogoList);
        str.append(" && 卡logo.contains(\"123\") ");
        long totalTime = 0;

        for (int i = 0 ; i < 1000 ; i++ ){

            long start = System.currentTimeMillis();

            Object obj = grvyShell.evaluate(str.toString());
            long time = System.currentTimeMillis() - start;
            totalTime += time;
            System.out.println("obj: "+obj+" cost time:" + time);



        }
        System.out.println("grvy shell total time: " + totalTime);
    }
}
