package com.guce.groovy.listener;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.FileFilter;

public class FileListener extends FileAlterationListenerAdaptor {

    @Override
    public void onDirectoryCreate(File directory) {
        System.out.println("onDirectoryCreate");
    }

    @Override
    public void onDirectoryChange(File directory) {
        System.out.println("onDirectoryChange");
    }

    @Override
    public void onDirectoryDelete(File directory) {
        System.out.println("onDirectoryDelete");
    }

    @Override
    public void onFileCreate(File file) {
        System.out.println(file.lastModified());
        System.out.println("onFileCreate,file=" + file.getName());
    }

    @Override
    public void onFileChange(File file) {
        System.out.println("onFileChange,file=" + file.getName());
    }

    @Override
    public void onFileDelete(File file) {
        System.out.println("onFileDelete,file=" + file.getName());
    }

    public static void main(String[] args) throws Exception {

        FileListener myFileLister = new FileListener();
        String path = "/Users/guchengen495/workspace/github/JHodgepodge/api/src/main/java/com/guce/groovy";
        File file = new File(path);
        System.out.println(file.exists());
        FileAlterationObserver observer1 = new FileAlterationObserver(file, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if ("GroovyDemo.groovy".equals(pathname.getName())){
                    return true;
                }
                return false;
            }
        });
        observer1.addListener(myFileLister);

        FileAlterationMonitor monitor = new FileAlterationMonitor(10000);
        monitor.addObserver(observer1);

        monitor.start();
    }
}
