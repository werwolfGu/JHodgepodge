package com.guce.dp;

/**
 * @Author chengen.gce
 * @DATE 2020/9/12 10:47 下午
 * 0-1 背包问题
 * 问题描述：
 * 有5个钻石 价值分别是 1、6、18、22、28 ；重量分别是 1、2、5、6、7
 * 假设你的背包容量为11 ；请问最多能装价值多少的钻石
 *
 * 思路：
 * 关注点：是否要将第i个商品放入背包中 判断是否要将第i个商品放入背包的先决条件：C >=w[i] ；
 * if C < w[i]
 *   f(i,C) = f(i -1,C)   //与放入第i- 1 个钻石时的价值一样； 因为i 个钻石已经放不下背包了；
 * else
 *   f(i,C) = max(f(i - 1 ,C ),f(i-1,C - w[i]) + v[i] ) // 放入第i个钻石时的价值是否比 i-1 的要大； 放入第i个钻石要留出w[i]的容量；
 *
 */
public class Knapsack {

    public static int solution(int[] w, int[] v, int C){

        int size = w.length;
        if (size == 0) {
            return 0;
        }

        int[][] dp = new int[size][C + 1];
        //初始化第一行
        //仅考虑容量为C的背包放第0个物品的情况
        for (int i = 0; i <= C; i++) {
            dp[0][i] = w[0] <= i ? v[0] : 0;
        }
        //填充其他行和列
        for (int i = 1; i < size; i++) {
            for (int j = 0; j <= C; j++) {

                dp[i][j] = dp[i - 1][j];
                if (w[i] <= j) {
                    dp[i][j] = Math.max(dp[i][j], v[i] + dp[i - 1][j - w[i]]);
                }
            }
        }
        return dp[size - 1][C];
    }

    public static void main(String[] args) {
        int[] v = new int[]{1,6,18,22,28};
        int[] w = new int[]{1,2,5,6,7};
        System.out.println(solution(w,v,11));
    }
}
