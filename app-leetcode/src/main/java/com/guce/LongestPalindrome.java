package com.guce;

/**
 * Created by chengen.gu on 2018/9/18.
 * <p>
 * 最长回文子串
 * <p>
 * 示例 1：
 * 输入: "babad"
 * 输出: "bab"
 * 注意: "aba"也是一个有效答案。
 * <p>
 * 示例 2：
 * 输入: "cbbd"
 * 输出: "bb"
 */
public class LongestPalindrome {

    /**
     * 中心扩展法
     *
     * @param s
     * @return
     */
    public static String solution(String s) {
        int max = 0;
        int idx = 0;

        for (int i = 0; i < s.length(); i++) {
            int tmp = expend(s, i, i);
            int tmp1 = expend(s, i, i + 1);

            if (Math.max(tmp, tmp1) > max) {
                max = Math.max(tmp, tmp1);
                idx = i - (max - 1) / 2;
            }

        }
        return s.substring(idx, max + idx);
    }

    public static int expend(String str, int l, int r) {
        while (l >= 0 && r < str.length() && str.charAt(l) == str.charAt(r)) {
            l--;
            r++;
        }
        return r - l - 1;
    }

    /**
     * abcdedcba
     * l   r
     * 如果 dp[l, r] = true 那么 dp[l + 1, r - 1] 也一定为 true
     * 关键在这里：[l + 1, r - 1] 一定至少有 2 个元素才有判断的必要
     * 因为如果 [l + 1, r - 1] 只有一个元素，不用判断，一定是回文串
     * 如果 [l + 1, r - 1] 表示的区间为空，不用判断，也一定是回文串
     * [l + 1, r - 1] 一定至少有 2 个元素 等价于 l + 1 < r - 1，即 r - l >  2
     * <p>
     * 写代码的时候这样写：如果 [l + 1, r - 1]  的元素小于等于 1 个，即 r - l <=  2 ，就不用做判断了
     * <p>
     * 因为只有 1 个字符的情况在最开始做了判断
     * 左边界一定要比右边界小，因此右边界从 1 开始
     *
     * @param s
     * @return
     */
    public static String dpSolution(String s) {
        int size = s.length();
        if (size < 2) {
            return s;
        }
        boolean dp[][] = new boolean[size][size];
        int maxLen = 1, start = 0;
        for (int i = 0; i < size; i++) {
            dp[i][i] = true;
            for (int j = 0; j < i; j++) {
                if (s.charAt(i) == s.charAt(j) && (i - j == 1 || dp[j + 1][i - 1])) {
                    dp[i][j] = true;
                    if (i - j + 1 > maxLen) {
                        maxLen = i - j + 1;
                        start = j;
                    }
                }
            }
        }
        return s.substring(start, start + maxLen);
    }


    public static void main(String[] args) {
        System.out.println(solution("dhabdddddd"));
        char i = '1';
        char a = 'a';
        if (i > 48 && i < 57) {
            System.out.println("a " + (int) a + "  1:" + (int) '9' + "i" + " 0 :" + (int) '0');
        }
    }
}
