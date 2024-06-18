package com.guce.dp;

/**
 * https://leetcode.cn/problems/coin-change/description/
 * 零钱兑换
 * @Author chengen.gce
 * @DATE 2024/6/15 16:30
 */
public class CoinChange {

    public int coinChange(int[] coins, int amount) {

        int max = amount + 1;
        int[] dp = new int[max];
        dp[0] = 0;
        for (int i = 1; i < max; i++) {
            dp[i] = max;
        }
        for (int i = 1; i <= amount; i++) {
            for (int j = 0; j < coins.length; j++) {
                if (coins[j] <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coins[j]] + 1);
                }
            }
        }
        return dp[amount] == max ? -1 : dp[amount];
    }
}
