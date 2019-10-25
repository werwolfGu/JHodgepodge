package com.guce.thread;

import java.util.BitSet;

/**
 * Created by chengen.gu on 2019/10/9.
 */
public class NotifyDemo {
    private volatile boolean go = false;

    public static void main(String[] args) {
        int [] array = new int [] {1,2,3,22,0,3,19999};
        BitSet bitSet  = new BitSet();
        //将数组内容组bitmap
        for(int i=0;i<array.length;i++)
        {
            bitSet.set(array[i], true);
        }
        System.out.println(bitSet.size());
        System.out.println(bitSet.get(19999));
    }
}
