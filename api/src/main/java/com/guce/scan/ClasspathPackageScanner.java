package com.guce.scan;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chengen.gu on 2019/9/19.
 */
public class ClasspathPackageScanner {

    private static Logger log = LoggerFactory.getLogger(ClasspathPackageScanner.class);

    private String basePackage ;
    private ClassLoader classLoader;


    public static void main(String[] args) {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("grvy-config.properties");

        try {
            String str = IOUtils.toString(in,"UTF-8");
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
