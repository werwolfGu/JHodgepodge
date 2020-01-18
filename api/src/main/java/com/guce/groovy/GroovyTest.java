package com.guce.groovy;

import com.guce.groovy.engine.GrvyClassLoader;

import java.io.File;
import java.io.IOException;

public class GroovyTest {
    public static void main(String[] args) throws IllegalAccessException, IOException, InstantiationException, InterruptedException {

        String path = "/Users/guchengen495/workspace/github/JHodgepodge/api/src/main/java/com/guce/groovy/GroovyDemo.groovy";
        File file = new File(path);
        IFoo demo = GrvyClassLoader.loadClass(path);
        long last = file.lastModified();
        for (int i = 0 ; i < 10 ; i++ ){

            long curr = file.lastModified();
            if ( curr != last){
                demo = GrvyClassLoader.loadClass(path);
                last = curr;
            }
            demo.print();

            Thread.sleep(5000L);
        }


    }
}
