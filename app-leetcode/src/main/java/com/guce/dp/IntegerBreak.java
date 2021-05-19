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
     *
     * 对于的正整数 nn，当 n \ge 2n≥2 时，可以拆分成至少两个正整数的和。令 kk 是拆分出的第一个正整数，则剩下的部分是 n-kn−k，n-kn−k 可以不继续拆分，或者继续拆分成至少两个正整数的和。由于每个正整数对应的最大乘积取决于比它小的正整数对应的最大乘积，因此可以使用动态规划求解。
     *
     * 创建数组 \textit{dp}dp，其中 \textit{dp}[i]dp[i] 表示将正整数 ii 拆分成至少两个正整数的和之后，这些正整数的最大乘积。特别地，00 不是正整数，11 是最小的正整数，00 和 11 都不能拆分，因此 \textit{dp}[0]=\textit{dp}[1]=0dp[0]=dp[1]=0。
     *
     * 当 i \ge 2i≥2 时，假设对正整数 ii 拆分出的第一个正整数是 jj（1 \le j < i1≤j<i），则有以下两种方案：
     *
     * 将 ii 拆分成 jj 和 i-ji−j 的和，且 i-ji−j 不再拆分成多个正整数，此时的乘积是 j \times (i-j)j×(i−j)；
     *
     * 将 ii 拆分成 jj 和 i-ji−j 的和，且 i-ji−j 继续拆分成多个正整数，此时的乘积是 j \times \textit{dp}[i-j]j×dp[i−j]。
     *
     * 因此，当 jj 固定时，有 \textit{dp}[i]=\max(j \times (i-j), j \times \textit{dp}[i-j])dp[i]=max(j×(i−j),j×dp[i−j])。由于 jj 的取值范围是 11 到 i-1i−1，需要遍历所有的 jj 得到 \textit{dp}[i]dp[i] 的最大值，因此可以得到状态转移方程如下：
     *
     * \textit{dp}[i]=\mathop{\max}\limits_{1 \le j < i}\{\max(j \times (i-j), j \times \textit{dp}[i-j])\}
     * dp[i]=
     * 1≤j<i
     * max
     * ​
     *  {max(j×(i−j),j×dp[i−j])}
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
