package com.guce.groovy.engine;

import groovy.lang.GroovyObject;

public class GroovyMethodExecEngine {


    public static <T> T invokeMethod(GroovyObject grvyObj,String methodName,Object args){
        return (T) grvyObj.invokeMethod(methodName, args);
    }


}
