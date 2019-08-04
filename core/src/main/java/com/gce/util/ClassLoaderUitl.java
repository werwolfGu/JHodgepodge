package com.gce.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chengen.gu on 2019/5/11.
 */
public abstract class ClassLoaderUitl {

    private static Logger logger = LoggerFactory.getLogger(ClassLoader.class);

    public static URL[] getResource(String... resourceNames){

        Set<URL> urlSet = new HashSet<>();
        for(String resourceName : resourceNames ){
            boolean found = false;
            //从当前线程的classloader中找
            found = getResources(urlSet,resourceName,Thread.currentThread().getContextClassLoader(),false);
            //从装载自己的classloader中找
            if (!found){
                found = getResources(urlSet,resourceName,ClassLoaderUitl.class.getClassLoader(),false);
            }
            //从系统classLoader中找
            if (!found){
                found = getResources(urlSet,resourceName,null,false);
            }
        }

        URL[] urls = new URL[urlSet.size()];
        return urlSet.toArray(urls);
     }

     public static boolean getResources(Set<URL> urlSet,String resourceName,ClassLoader classLoader,boolean sysClassLoader){

        if (resourceName == null ){
            return false;
        }
        try {
            Enumeration<URL> enumeration = null;
            if (classLoader != null) {
                enumeration = classLoader.getResources(resourceName);

            }else if (sysClassLoader ){
                enumeration = ClassLoader.getSystemResources(resourceName);
            }

            if(enumeration != null && enumeration.hasMoreElements()){
                while (enumeration.hasMoreElements()){
                    urlSet.add(enumeration.nextElement());
                }
                return true;
            }
        } catch (IOException e) {
            logger.error("classloader exception :{}",e.getMessage(),e);
        }
        return false;
     }
}
