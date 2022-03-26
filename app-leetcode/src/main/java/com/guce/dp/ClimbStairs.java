package com.guce.dp;

/**
 * @Author chengen.gce
 * @DATE 2020/9/11 11:16 下午
 * https://leetcode-cn.com/problems/climbing-stairs/
 * 爬楼梯
 */
public class ClimbStairs {

    public int climbStairs(int n) {

        return dp(n);
    }

    /**
     * 递归
     * 递归实现方式：一个问题转换成子问题  dp[n] = dp[n - 1] + dp[n - 2]
     * @param n
     * @return
     */
    public int dp(int n){
        if (n == 1){
            return 1;
        }
        if (n == 2){
            return 2;
        }
        return dp(n -1) + dp(n -2);
    }

    public int climbStairs1(int n){
        if ( n == 1){
            return 1;
        }

        if (n == 2 ){
            return 2;
        }
        int x = 1 , y = 2,r = 0;
        for (int i = 3 ; i <= n ; i++ ){
            r = x + y;
            x = y ;
            y = r;
        }
        return r;
    }

    /**
     * 类似斐波那契
     * @param n
     * @return
     */
    public int climbStairs2(int n) {

        int first = 0 , second = 0 ,result = 1;
        for (int i = 1 ; i <= n ; i++ ){
            first = second ;
            second = result ;
            result = first +second ;

        }
        return result;
    }

    public static void main(String[] args) {

        ClimbStairs climbStairs = new ClimbStairs();
        long start = System.currentTimeMillis();
        System.out.println(climbStairs.climbStairs(45));
        long second = System.currentTimeMillis();

        System.out.println(climbStairs.climbStairs2(45));

        System.out.println("1-> " + (second - start) + "  ; 2 -> " + (System.currentTimeMillis() - second));

        System.out.println(climbStairs.climbStairs1(45));



    }
}
