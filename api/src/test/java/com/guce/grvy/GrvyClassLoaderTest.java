package com.guce.grvy;

import com.guce.groovy.engine.GrvyClassLoader;
import com.guce.groovy.manager.GroovyDynamicsScriptManager;
import com.guce.grvy.service.IGrvy;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            grvy = (IGrvy) GrvyClassLoaderTest.class.getClassLoader().loadClass("com.guce.grvy.service.impl.GrvyImpl").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println(grvy.printGrvy("grvy"));
    }

    @Test
    public void testGrvyClassLoader(){
        String clazzpath = "/Users/guchengen495/workspace/github/JHodgepodge/api/src/test/java/com/guce/grvy/service/impl/GrvyImpl.groovy";
        try {

            IGrvy iGrvy = GrvyClassLoader.loaderInstance(clazzpath);;
            String name = iGrvy.printGrvy(" grvy classloader");
            System.out.println(name);
            //IGrvy iGrvy = GrvyClassLoader.loaderInstance(clazzpath);
           } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
