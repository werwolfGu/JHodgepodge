package com.guce.dp;

/**
 * https://leetcode-cn.com/problems/ba-shu-zi-fan-yi-cheng-zi-fu-chuan-lcof/
 * 数字翻译成字母 ， 0 -> a , 1 -> b , .... 25 -> z
 * @Author chengen.gce
 * @DATE 2021/3/30 2:21 下午
 */
public class TranslateNum {

    /**
     * 数字翻译成字母 ， 0 -> a , 1 -> b , .... 25 -> z
     * num 能翻译成多少种字母
     * @param num
     * @return
     */
    public static int translateNum(int num){
        String s = String.valueOf(num);
        int[] dp = new int[s.length() + 1];
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2 ; i <= s.length() ; i++ ){
            char x = s.charAt(i -1);
            char y = s.charAt(i - 2);
            if (y =='1' || y == '2' && x <='5'){
                dp[i] = dp[i-1] + dp[i - 2];
            }else {
                dp[i] = dp [i -1];
            }
        }
        return dp[s.length()];
    }

    /**
     * 数字翻译成字母 ， 1 -> a , 1 -> b , .... 26 -> z
     * num 能翻译成多少种字母
     * @param num
     * @return
     */
    public static int translateNum1(int num){
        String s = String.valueOf(num);
        int[] dp = new int[s.length() + 1];
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2 ; i <= s.length() ; i++ ){
            char x = s.charAt(i -1);
            char y = s.charAt(i - 2);
            if (y =='1' || y == '2' && x <='6'){
                if ( x == '0'){
                    dp[i] = dp[i - 2];
                } else {
                    dp[i] = dp[i-1] + dp[i - 2];
                }

            }else if (y == 0){
                /////00 30 40 .... 是没有对应的字母的
                return -1;
            }else {
                dp[i] = dp[i - 1];
            }
        }
        return dp[s.length()];
    }

    public static void main(String[] args) {
        System.out.println(translateNum(25));
    }
}
