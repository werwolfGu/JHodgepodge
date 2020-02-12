package com.guce.guava;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;

/**
 * @Author chengen.gu
 * @DATE 2020/2/10 10:01 下午
 */
public class GuavaFileDemo {

    public static void main(String[] args) throws IOException {

        //apache file
        File file = new File("dd");
        LineIterator it = FileUtils.lineIterator(file, "UTF-8");
        while (it.hasNext()){
            it.next();
        }

    }
}
