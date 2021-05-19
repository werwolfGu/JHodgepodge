package com.guce.dp;

/**
 * @Author chengen.gce
 * @DATE 2021/3/30 1:34 下午
 * https://leetcode-cn.com/problems/interleaving-string/
 *
 */
public class IsInterleave {

    public  boolean isInterLeave(String s1 ,String s2,String s3){
        int  m = s1.length(), n = s2.length();
        boolean[][] dp = new boolean[m + 1][n + 1];

        dp[0][0] = true;

        for (int i = 0 ; i <= m ;i++ ){
            for (int j = 0 ; j <= n ; j++){
                int idx = i + j -1 ;
                if (i > 0){
                    dp[i][j] = dp[i][j] || (dp[i - 1][j] && (s1.charAt(i-1) == s3.charAt(idx)));
                    //dp[i][j] = dp[i][j] || (dp[i - 1][j] && s1.charAt(i - 1) == s3.charAt(idx));
                }
                if (j > 0){
                    dp[i][j] = dp[i][j] || (dp[i][j - 1] && (s2.charAt(j - 1)== s3.charAt(idx)));
                }
            }
        }

        return dp[m][n];
    }

    public static boolean isInterleave(String s1, String s2, String s3) {
        int len1 = s1.length();
        int len2 = s2.length();
        int len3 = s3.length();
        if (len1  + len2 != len3){
            return false;
        }
        boolean[][] dp = new boolean[len1 + 1][len2 + 1];
        dp[0][0] = true;
        for (int i = 1 ; i <= len1 ; i++){
            dp[i][0] = dp[i -1][0] && (s1.charAt(i -1)==s3.charAt(i-1));
        }

        for (int i = 1 ; i <= len2 ; i++){
            dp[0][i] = dp[0][i -1] && (s2.charAt(i -1)==s3.charAt(i-1));
        }

        for (int i = 1 ; i <= len1 ; i++ ){
            for (int j = 1 ; j <= len2 ;j++ ){
                int idx = i + j - 1;
                dp[i][j] = (dp[i -1][j] && (s1.charAt(i -1 ) == s3.charAt(idx))) ||
                        (dp[i][j - 1] && (s2.charAt(j -1) == s3.charAt(idx))) ;
            }
        }
        return dp[len1][len2];
    }

    public static void main(String[] args) {

        IsInterleave isInterleave = new IsInterleave();
        System.out.println(isInterleave.isInterLeave("aabcc","dbbca","aadbbcbcac"));
        System.out.println(isInterleave.isInterLeave("aabcc","dbbca","aadbbbaccc"));
        System.out.println(isInterleave.isInterleave("aabcc","dbbca","aadbbcbcac"));
    }
}
