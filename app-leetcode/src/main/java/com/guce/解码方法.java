package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2021/5/25 11:28 下午
 */
public class 解码方法 {

    public int numDecodings(String s) {

        int[] dp = new int[s.length() + 1];
        dp[0] = 1;
        dp[1] = s.charAt(0) == '0' ? 0 : 1;
        for ( int i = 2 ; i <= s.length() ; i++ ){
            //xy  如果 x= 1 或 2 && y < 7
            // 当 y == 0 时 dp[i] = dp[i-2]
            // 否则   dp[i] = dp[i -1] + dp[i - 2]
            char x = s.charAt(i - 2);
            char y = s.charAt(i - 1);
            if ( x == '1' || x == '2' && y < '7'){
                if ( y == '0'){
                    dp[i] = dp[i - 2];
                } else {
                    dp[i] = dp[i-1] + dp[i-2];
                }
            } else if (y == '0') {
                /////   00 ,30 ,40 ....
                return 0;
            }else {
                /////
                dp[i] = dp[i -1];
            }
        }
        return dp[s.length()];
    }
}
