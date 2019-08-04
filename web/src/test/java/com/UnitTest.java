package com;

import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by chengen.gu on 2018/10/30.
 */
public class UnitTest {

    @Test
    public void test(){

    }
    public static void main(String[] args) {
        System.out.println( Integer.highestOneBit(7));
        System.out.println( 5 >> 1);
        System.out.println( Integer.decode("132"));
        Integer i = 523;
        Integer j = 523;
        System.out.println(i == j);
        assert false;

        Executor e = Executors.newSingleThreadExecutor();
    }
}
