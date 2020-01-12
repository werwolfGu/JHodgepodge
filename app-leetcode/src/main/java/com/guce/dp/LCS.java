package com.guce.dp;

/**
 * Created by chengen.gu on 2019/10/23.
 * 最长公共子序列  使用动态规划
 */
public class LCS {

    public int solution(final String str1, final String str2) {

        if (str1 == null || str1.length() <= 0) {
            return 0;
        }

        if (str2 == null || str2.length() <= 0) {
            return 0;
        }

        int dp[][] = new int[str1.length() + 1][str2.length() + 1];

        int m = str1.length(), n = str2.length();
        for (int i = 0; i < m; i++) {
            dp[i][0] = 0;
        }
        for (int j = 0; j < n; j++) {
            dp[0][j] = 0;
        }
        for (int i = 1; i <= str1.length(); i++) {
            for (int j = 1; j <= str2.length(); j++) {

                if (str1.charAt(i) == str2.charAt(j)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j]);
                }
            }
        }
        return dp[str1.length()][str2.length()];
    }

    /**
     * 最长前缀匹配
     *
     * @param strs
     * @return
     */
    public String longestPrefixMatch(String[] strs) {
        String prefix = strs[0];
        for (int i = 1; i < strs.length; i++) {
            StringBuilder tmp = new StringBuilder();
            int len = Math.min(prefix.length(), strs[i].length());
            for (int j = 0; j < len; j++) {

                if (prefix.charAt(j) == strs[i].charAt(j)) {
                    tmp.append(prefix.charAt(j));
                }
            }
            prefix = tmp.toString();
        }
        return prefix;
    }

    public static void main(String[] args) {
        System.out.println(false ^ false);

    }
}
