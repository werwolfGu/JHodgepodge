package com.guce;

import static com.sun.xml.internal.fastinfoset.util.ValueArray.MAXIMUM_CAPACITY;

/**
 * @Author chengen.gce
 * @DATE 2021/10/28 10:38 下午
 */
public class HashMapDemo {

    /**
     * 列表大小   向下取 2 的幂次方  比如 7 返回 8  ； 17 返回 32
     * @param cap
     * @return
     */
    public static int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    public static void main(String[] args) {
        System.out.println(tableSizeFor(17));
    }
}
