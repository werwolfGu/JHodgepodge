package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2021/3/22 9:17 下午
 */
public class HammingWeight {

    public int hammingWeight(int n) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            if ( (n & ( 1 << i)) != 0){
                res++ ;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        HammingWeight weight = new HammingWeight();
        System.out.println(weight.hammingWeight(3));

    }
}
