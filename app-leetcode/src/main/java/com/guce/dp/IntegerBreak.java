package com.guce.dp;

/**
 * @Author chengen.gce
 * @DATE 2021/3/1 10:29 下午
 *
 * https://leetcode-cn.com/problems/integer-break/
 *
 * 给定一个正整数 n，将其拆分为至少两个正整数的和，并使这些整数的乘积最大化。 返回你可以获得的最大乘积。
 *
 * 示例 1:
 *
 * 输入: 2
 * 输出: 1
 * 解释: 2 = 1 + 1, 1 × 1 = 1。
 * 示例 2:
 *
 * 输入: 10
 * 输出: 36
 * 解释: 10 = 3 + 3 + 4, 3 × 3 × 4 = 36。
 *
 *
 *
 */
public class IntegerBreak {
    /**
     * 动态规划
     *状态方程
     * 当i >= 2 假设将正整数i拆分出第一个正整数是 j ( 1 <= j < i) z则出现2中方案：
     *      1. 拆分成 j 和 i - j 的和，i - j 不再拆分，那么乘积为 j * (i - j)
     *      2. 拆分成 j 和 i - j 的和，i - j 继续拆分，那么乘积为 j * dp[i - j]
     *
     * 那么得出状态方程式
     *    dp[i] =  Math.max(j * (i - j),j * dp[i - j]);
     * 当 i 继续拆分时：
     *    dp[i] = Math.max( dp[i], Math.max(j * (i - j),j * dp[i - j]) );
     * @param n
     * @return
     */
    public static int integerBreak(int n){

        int dp[] = new int[n +1] ;

        for (int i = 2 ; i <= n ; i++ ){

            for (int j = 1 ; j < i ; j++ ){
                dp[i] = Math.max(dp[i],Math.max(j * (i - j),j * dp[i - j]));
            }
            //dp[i] = currMax;
        }
        return dp[n];
    }


    public static int integerBreak1( int n){
        if(n <= 3) return n - 1;
        int a = n / 3, b = n % 3;
        if(b == 0) return (int)Math.pow(3, a);
        if(b == 1) return (int)Math.pow(3, a - 1) * 4;
        return (int)Math.pow(3, a) * 2;
    }

    public static void main(String[] args) {
        System.out.println(integerBreak(2));
        System.out.println(integerBreak1(2));
    }
}
