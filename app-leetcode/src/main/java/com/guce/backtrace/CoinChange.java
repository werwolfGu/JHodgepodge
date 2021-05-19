package com.guce.backtrace;

import java.util.Arrays;

/**
 * @Author chengen.gce
 * @DATE 2021/3/20 6:31 下午
 * https://leetcode-cn.com/problems/coin-change/
 * 零钱兑换
 */
public class CoinChange {

    private int res = 0 ;

    public  int coinChange(int[] coins, int amount) {
        res = amount + 1;
        Arrays.sort(coins);
        backTrace(coins, 0 , 0 , amount);
        if ( res == (amount + 1 )){
            return -1;
        }
        return res;
    }

    public void backTrace (int[] coins, int size ,long currAmount ,int amount ){
        if ( currAmount == amount) {
            res = Math.min(res,size);
            return ;
        }
        if (currAmount > amount){
            return ;
        }

        for (int i = 0 ; i < coins.length ;i++ ){
            if (currAmount +coins[i] > amount ){
                break;
            }
            currAmount = currAmount + coins[i];
            size++ ;
            backTrace(coins , size ,currAmount ,amount);
            currAmount = currAmount - coins[i];
            size--;
        }
    }

    ////dp
    public  int coinChangeDp(int[] coins, int amount) {

        int[] dp = new int[amount + 1];
        Arrays.fill(dp,amount + 1);
        dp[0] = 0 ;
        for (int i = 1 ; i <= amount ; i++){

            for (int j = 0 ; j < coins.length ; j++ ){
                if ( i >= coins[j]){
                    dp[i] = Math.min(dp[i],dp[i -coins[j]] + 1);
                }
            }
        }
        return dp[amount];
    }

    public static void main(String[] args) {
        CoinChange coinChange = new CoinChange();
        int[] coins = new int[]{1,2,5} ;
        //System.out.println(coinChange.coinChange(coins,50));

        System.out.println(coinChange.coinChangeDp(coins,50));
    }
}
