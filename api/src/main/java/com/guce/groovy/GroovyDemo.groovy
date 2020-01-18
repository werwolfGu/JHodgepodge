
package com.guce.groovy

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.guce.groovy.model.RequestEntry
import com.guce.groovy.model.ResponseEntry

class GroovyDemo implements IFoo{

    void print() {
        System.out.println("abello word!!dddddsssdddd");
    }

    List<String> printArgs(String str1, String str2, String str3) {
        String jsonString = "[\""+str1+"\",\""+str2+"\",\""+str3+"\"]";
        println("dasdhadhhhhhhhhhhh")
        return JSON.parseObject(jsonString, new TypeReference<List<String>>() {});
    }


    ResponseEntry printEntryArgs(RequestEntry req , ResponseEntry resp){
        println(req)
        resp.setId(req.getUserId())
        resp.setName(req.getUserName())
    }

}