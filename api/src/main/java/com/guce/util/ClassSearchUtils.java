package com.guce.util;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author chengen.gce
 * @DATE 2020/3/11 10:59 下午
 * <p>
 * <p>
 * 可以参考  ClassPathBeanDefinitionScanner
 */
@Slf4j
public class ClassSearchUtils {

    private static final String POINT = ".";

    private static final String KLASS_SUFFIX = ".class";

    private final static String FILE_PROTOCOL = "file";
    private final static String JAR_PROTOCOL = "jar";


    public static List<Class<?>> searchKlassList(String packageName) throws IOException {
        Set<String> klassPathList = getClassName(packageName, true);

        if (CollectionUtils.isEmpty(klassPathList)) {
            return null;
        }
        List<Class<?>> klassList = Lists.newArrayListWithCapacity(klassPathList.size());

        klassPathList.stream().forEach(klassPath -> {
            try {
                Class klass = ClassUtils.getClassLoder().loadClass(klassPath);
                klassList.add(klass);
            } catch (ClassNotFoundException e) {
                log.error("无法加载到类：classpath:{}", klassPath, e);
            }
        });
        return klassList;
    }

    public static Set<String> getClassName(String packageName, boolean childPackage) throws IOException {
        Set<String> fileNames = new HashSet<>();
        ClassLoader loader = ClassUtils.getClassLoder();
        String packagePath = packageName.replace(POINT, File.separator);
        Enumeration<URL> urls = loader.getResources(packagePath);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if (url == null) {
                continue;
            }
            String type = url.getProtocol();
            switch (type) {

                case FILE_PROTOCOL:
                    fileNames.addAll(getClassNameByFile(packageName, url.getPath(), childPackage));
                    break;
                case JAR_PROTOCOL:
                    fileNames.addAll(getClassNameByJar(url.getPath(), childPackage));
                    break;
                default:

            }

        }
        return fileNames;
    }


    private static List<String> getClassNameByFile(String packagePath, String filePath, boolean childPackage) throws UnsupportedEncodingException {
        List<String> myClassName = new ArrayList<>();

        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        if (childFiles == null) {
            return myClassName;
        }
        for (File childFile : childFiles) {

            if (childFile.isDirectory()) {
                if (childPackage) {
                    String currentPathName = childFile.getAbsolutePath().substring(childFile.getAbsolutePath().lastIndexOf(File.separator) + 1);
                    currentPathName = packagePath + "." + currentPathName;
                    myClassName.addAll(getClassNameByFile(currentPathName, childFile.getPath(), childPackage));
                }
            } else {
                String klassName = childFile.getName();
                if (klassName.endsWith(KLASS_SUFFIX)) {
                    klassName = packagePath + "." + klassName.replace(KLASS_SUFFIX, "");
                    myClassName.add(klassName);
                }
            }
        }
        return myClassName;
    }

    private static List<String> getClassNameByJar(String jarPath, boolean childPackage) {
        List<String> myClassName = new ArrayList<>();
        String[] jarInfo = jarPath.split("!");
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
        String packagePath = jarInfo[1].substring(1);
        try {
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(KLASS_SUFFIX)) {
                    if (childPackage) {
                        if (entryName.startsWith(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    } else {
                        int index = entryName.lastIndexOf("/");
                        String myPackagePath;
                        if (index != -1) {
                            myPackagePath = entryName.substring(0, index);
                        } else {
                            myPackagePath = entryName;
                        }
                        if (myPackagePath.equals(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
        return myClassName;
    }

    private static List<String> getClassNameByJars(URL[] urls, String packagePath, boolean childPackage) throws UnsupportedEncodingException {
        List<String> myClassName = new ArrayList<String>();
        if (urls != null) {
            for (int i = 0; i < urls.length; i++) {
                URL url = urls[i];
                String urlPath = url.getPath();
                // 不必搜索classes文件夹
                if (urlPath.endsWith("classes/")) {
                    continue;
                }
                String jarPath = urlPath + "!/" + packagePath;
                myClassName.addAll(getClassNameByJar(jarPath, childPackage));
            }
        }
        return myClassName;
    }

    private static Set<Class<?>> getClassNameByJar(String jarPath) {
        Set<Class<?>> myClassName = new HashSet<>();
        String[] jarInfo = jarPath.split("!");
        String jarFilePath = jarPath;
        File file = new File(jarFilePath);
        try {
            URL url = file.toURI().toURL();

            ////加载外部类使用 URLClassLoader
            URLClassLoader classLoader =
                    new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String entryName = jarEntry.getName();

                if (entryName.endsWith(KLASS_SUFFIX)) {
                    entryName = entryName.replace(".class", "").replaceAll("/", ".");
                    Class<?> clazz = classLoader.loadClass(entryName);
                    myClassName.add(clazz);
                }
            }
        } catch (Exception e) {

        }
        return myClassName;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {

       /* String basePackage = "com.guce.uniqid";
        List<Class<?>> classes = searchKlassList(basePackage);*/
        String basePackage = "/Users/chengen.gu/.m2/repository/org/yaml/snakeyaml/1.25/snakeyaml-1.25.jar";
        File file = new File(basePackage);
        URL url = file.toURI().toURL();

        Set<Class<?>> list = getClassNameByJar(basePackage);
        System.out.println(list);
        //判断子类是否继承自 父类或接口  clazz.isAssignableFrom(子类名称)


    }
}
