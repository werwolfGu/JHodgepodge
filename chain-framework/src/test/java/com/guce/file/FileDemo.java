package com.guce.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.Collection;

/**
 * @Author chengen.gu
 * @DATE 2020/2/14 6:00 下午
 */
public class FileDemo {
    public static void main(String[] args) {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        File  file = new File(path + File.separator + "node" );
        File[] arr = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                System.out.println(pathname);
                return false;
            }
        });
        Collection<File> listFiles = FileUtils.listFiles(file, FileFilterUtils.suffixFileFilter("Flow.json"),null);
        System.out.println(file);
    }
}
