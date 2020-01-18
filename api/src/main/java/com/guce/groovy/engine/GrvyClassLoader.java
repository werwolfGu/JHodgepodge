package com.guce.groovy.engine;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.apache.commons.io.IOUtils;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class GrvyClassLoader {

    private static String CHATSET = "UTF-8";
    private static GroovyClassLoader groovyClassLoader ;

    static{
        CompilerConfiguration config = new CompilerConfiguration();
        config.setSourceEncoding(CHATSET);
        // 设置该GroovyClassLoader的父ClassLoader为当前线程的加载器(默认)
        groovyClassLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader(), config);
    }


    public static <T> T  loadClass(String filePath) throws IllegalAccessException, IOException, InstantiationException {
        File file = new File(filePath);
        return loadClass(file);
    }
    public static <T> T loadClass(File file) throws IOException, IllegalAccessException, InstantiationException {

        String grvyContent = IOUtils.toString(new FileInputStream(file),CHATSET);
        Class groovyClass = groovyClassLoader.parseClass(grvyContent);

        return (T) groovyClass.newInstance();
    }

}
