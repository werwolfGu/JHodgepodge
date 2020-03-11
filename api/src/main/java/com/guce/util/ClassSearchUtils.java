package com.guce.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author chengen.gce
 * @DATE 2020/3/11 10:59 下午
 */
public class ClassSearchUtils {

    public static List<Class> searchProjClass(String basePackage){

        String clazzPath = Thread.currentThread().getClass().getResource("/").getPath();

        basePackage = basePackage.replace(".", File.separator);
        doPath(basePackage,null);
        return null;
    }

    private static void doPath(String path,List<String> clazzList){
         doPath(new File(path),clazzList);
    }

    private static void doPath(File file,List<String> clazzList) {

        if (file.isDirectory()) {//文件夹
            //文件夹我们就递归
            File[] files = file.listFiles();
            for (File f1 : files) {
                doPath(f1,clazzList);
            }
        } else {//标准文件
            //标准文件我们就判断是否是class文件
            if (file.getName().endsWith(".class")) {
                //如果是class文件我们就放入我们的集合中。
                clazzList.add(file.getPath());
            }
        }
        return ;
    }

    public static void searchJarClass(String packagepath,List<String> clazzPathList) throws IOException, URISyntaxException {

        Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader()
                .getResources(packagepath.replace(".", "/"));
        while(urlEnumeration.hasMoreElements()){
            URL url = urlEnumeration.nextElement();
            String protocol = url.getProtocol();
            if ("jar".equalsIgnoreCase(protocol)) {
                JarURLConnection connection = (JarURLConnection) url.openConnection();
                if (connection != null) {
                    JarFile jarFile = connection.getJarFile();
                    if (jarFile != null) {
                        Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                        while (jarEntryEnumeration.hasMoreElements()) {
                            JarEntry entry = jarEntryEnumeration.nextElement();
                            String jarEntryName = entry.getName();
                            //这里我们需要过滤不是class文件和不在basePack包名下的类
                            if (jarEntryName.contains(".class") && jarEntryName.replaceAll("/",".").startsWith(packagepath)) {
                                String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".");
                                clazzPathList.add(className);
                            }
                        }
                    }
                }
            }else if("file".equals(protocol)){
                doPath(new File(url.toURI()),clazzPathList);
            }
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        String clazzPath = Thread.currentThread().getClass().getResource("/").getPath();
        String basePackage = "com.guce.thread";

        basePackage = basePackage.replace(".", File.separator);
        basePackage = clazzPath + basePackage;
        List<String> clazzpathList = new ArrayList<>();
        doPath(basePackage,clazzpathList);
        System.out.println(clazzpathList);
        List<String> jarClazzpathList = new ArrayList<>();
        searchJarClass("com.grvyframework.grvy.",jarClazzpathList);
        System.out.println(jarClazzpathList.size());
        System.out.println(jarClazzpathList);
    }
}
