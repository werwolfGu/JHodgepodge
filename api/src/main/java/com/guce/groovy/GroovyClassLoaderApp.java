package com.guce.groovy;

import com.google.common.collect.Maps;
import com.guce.groovy.model.RequestEntry;
import com.guce.groovy.model.ResponseEntry;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.apache.commons.io.IOUtils;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GroovyClassLoaderApp {

    private static GroovyClassLoader groovyClassLoader = null;

    public static void initGroovyClassLoader() {
        CompilerConfiguration config = new CompilerConfiguration();
        config.setSourceEncoding("UTF-8");
        // 设置该GroovyClassLoader的父ClassLoader为当前线程的加载器(默认)
        groovyClassLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader(), config);
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        loadClass();

        System.out.println("======================");
        for (int i = 0 ; i < 5; i++ ){
            execMethod();
            loadFile();
            Thread.sleep(10000L);
        }


    }

    /**
     * 通过类加载groovy
     */
    private static void loadClass(){
        GroovyObject groovyObject = null;
        try {
            groovyObject = (GroovyObject) GroovyClassLoaderApp.class.getClassLoader().loadClass("com.guce.groovy.GroovyDemo").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 执行无参函数
        groovyObject.invokeMethod("print",null);

        System.out.println("============================");

        // 执行有参函数
        Object[] objects = new Object[]{"abc", "def", "ghi"};
        List<String> ls=(List<String>) groovyObject.invokeMethod("printArgs", objects);
        ls.stream().forEach(System.out::println);

        RequestEntry req = new RequestEntry();
        req.setUserId("1234");
        req.setUserName("gce");
        ResponseEntry resp = new ResponseEntry();

        Object[] args = new Object[]{req,resp};
        groovyObject.invokeMethod("printEntryArgs",args);
        System.out.println("resp:" + resp);
    }


    private static void execMethod() throws IOException {
        File file = new File("/Users/guchengen495/workspace/github/JHodgepodge/api/src/main/java/com/guce/groovy/grvyFunFile.groovy");

        Map<Integer, String> map = Maps.newHashMap();
        map.put(0, "a");
        map.put(1, "b");

        String grvyContent = IOUtils.toString(new FileInputStream(file),"UTF-8");
        Class groovyClass = groovyClassLoader.parseClass(grvyContent);
        try {
            GroovyObject grvyObj = (GroovyObject) groovyClass.newInstance();
            System.out.println("addStr=" + grvyObj.invokeMethod("grvyFunFile", map));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
    /**
     * 通过文件路径加载groovy
     * @return
     */
    private static boolean loadFile() {
        File groovyFile = new File("/Users/guchengen495/workspace/github/JHodgepodge/api/src/main/java/com/guce/groovy/GroovyDemo.groovy");
        if (!groovyFile.exists()) {
            System.out.println("文件不存在");
            return false;
        }

        try {
            List<String> result;
            // 获得TestGroovy加载后的class
            Class<?> groovyClass = groovyClassLoader.parseClass(groovyFile);
            // 获得TestGroovy的实例
            GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
            // 反射调用printArgs方法得到返回值
            Object methodResult = groovyObject.invokeMethod("printArgs", new Object[]{"chy", "zjj", "xxx"});
            if (methodResult != null) {
                result = (List<String>) methodResult;
                result.stream().forEach(System.out::println);
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

}
