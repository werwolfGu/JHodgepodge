package com.guce.file;

import java.io.File;
import java.io.IOException;

/**
 * @Author chengen.gce
 * @DATE 2020/5/10 3:55 下午
 */
public class FileReaderDemo {
    public static void main( String[] args ) throws IOException
    {
        LargeFileReader handler = new LargeFileReader();

        File largeFile = new File("/Users/chengen.gu/Documents/资料/Redis设计与实现[带书签].pdf");
        for (int i = 0 ;  i< 2 ; i++ ){
            new Thread( () -> {
                handler.lockRead(largeFile);
            }).start();
        }


    }
}
