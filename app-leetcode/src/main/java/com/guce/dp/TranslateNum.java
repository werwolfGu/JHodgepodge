package com.guce.dp;

/**
 * @Author chengen.gce
 * @DATE 2021/3/30 2:21 下午
 */
public class TranslateNum {

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

    public static void main(String[] args) {
        System.out.println(translateNum(25));
    }
}
