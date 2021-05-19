package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/6/29 9:58 下午
 */
public class ClimbStairs {

    public static int climbStairs(int n){

        return recurrence(n);
    }

    /**
     * 递归
     * @param n
     * @return
     */
    public static int recurrence(int n){
        if (n == 1){
            return 1;
        }
        if (n == 2){
            return 2;
        }
        return recurrence(n - 1) + recurrence(n - 2);
    }

    /**
     * 迭代
     * @param n
     * @return
     */
    public static int climbStairs2(int n){

        int x = 1;
        int y = 2;
        int result = 0;
        for (int i = 2 ; i < n ; i++){
            result = x + y;
            x = y;
            y = result;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(climbStairs(4));
        System.out.println(climbStairs2(4));
    }
}
