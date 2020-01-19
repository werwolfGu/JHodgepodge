package com.guce.grvy;

import com.guce.groovy.engine.GrvyClassLoader;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @Author chengen.gu
 * @DATE 2020/1/19 11:41 下午
 */
public class GrvyClassLoaderTest {

    private static Logger logger = LoggerFactory.getLogger(GrvyClassLoaderTest.class);

    @Test
    public void testClassLoaderGrvyClass() throws ClassNotFoundException {
        IGrvy grvy = null;
        try {
            grvy = (IGrvy) GrvyClassLoaderTest.class.getClassLoader().loadClass("com.guce.grvy.GrvyImpl").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println(grvy.printGrvy("grvy"));
    }

    @Test
    public void testGrvyClassLoader(){
        String clazzPaht = "/Users/chengen.gu/apps/github/springboot-demo/api/src/test/java/com/guce/grvy/GrvyImpl.groovy";
        try {
            IGrvy iGrvy = GrvyClassLoader.loadClass(clazzPaht);
            System.out.println(iGrvy.printGrvy(" grvy load file"));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
