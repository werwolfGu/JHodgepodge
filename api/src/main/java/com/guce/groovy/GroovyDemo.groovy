
package com.guce.groovy

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference

class GroovyDemo{

    void print() {
        System.out.println("hello word!!!!ddddd");
    }

    List<String> printArgs(String str1, String str2, String str3) {
        String jsonString = "[\""+str1+"\",\""+str2+"\",\""+str3+"\"]";
        println("dasdhadh")
        return JSON.parseObject(jsonString, new TypeReference<List<String>>() {});
    }


}