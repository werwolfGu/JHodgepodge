package com.grvyframework.grvy.engine;

import groovy.lang.GroovyClassLoader;
import org.apache.commons.io.IOUtils;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class GrvyClassLoader {

    private static String CHARSET = "UTF-8";

    private static GroovyClassLoader groovyClassLoader ;

    static{
        CompilerConfiguration config = new CompilerConfiguration();
        config.setSourceEncoding(CHARSET);
        // 设置该GroovyClassLoader的父ClassLoader为当前线程的加载器(默认)
        groovyClassLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader(), config);
    }


    public static Class  loadClass(String filePath) throws  IOException {
        File file = new File(filePath);
        return loadClass(file);
    }
    public static Class loadClass(File file) throws IOException {

        String grvyContent = IOUtils.toString(new FileInputStream(file), CHARSET);
        Class groovyClass = groovyClassLoader.parseClass(grvyContent);

        return groovyClass;
    }

    public static <T> T loaderInstance(String filepath) throws IllegalAccessException, IOException, InstantiationException {

        Class clazz = loadClass(filepath);
        return (T) clazz.newInstance();
    }

    public static void main(String[] args) {

    }

}
