package com.guce;

/**
 * Created by chengen.gu on 2018/9/18.
 *
 * 最长回文子串
 *
 * 示例 1：
 输入: "babad"
 输出: "bab"
 注意: "aba"也是一个有效答案。

 示例 2：
 输入: "cbbd"
 输出: "bb"
 *
 */
public class LongestPalindrome {

    /**
     * 中心扩展法
     * @param s
     * @return
     */
    public static String solution(String s){
        int max = 0 ;
        int idx = 0 ;

        for (int i = 0 ; i < s.length() ; i++ ){
            int tmp = expend(s,i,i);
            int tmp1 = expend(s,i , i +1 );

            if (Math.max(tmp,tmp1) > max){
                max = Math.max(tmp,tmp1);
                idx =  i - (max -1) / 2;
            }

        }
        return s.substring(idx,max + idx );
    }

    public static int expend(String str , int l ,int r){
        while ( l >= 0 && r < str.length() && str.charAt(l) == str.charAt(r)){
            l-- ;
            r++ ;
        }
        return r - l - 1;
    }

    public static String dpSolution(String s){
        int size = s.length();
        if (size < 2 ){
            return s;
        }
        boolean dp[][] = new boolean[size][size];
        int maxLen =1 ,start = 0 ;
        for (int i = 0 ; i < size ; i++ ){
            dp[i][i] = true;
            for ( int j = 0 ; j < i ; j++ ){
                if ( s.charAt(i) == s.charAt(j) && (i - j == 1 || dp[j+1][i-1]) ){
                    dp[i][j] = true;
                    if (i - j +1 > maxLen){
                        maxLen = i - j +1 ;
                        start = j;
                    }
                }
            }
        }
        return s.substring(start,start + maxLen);
    }


    public static void main(String[] args) {
        System.out.println(solution("dhabdddddd"));
        char i = '1';
        char a = 'a';
        if(i > 48 && i < 57)
        System.out.println("a " + (int)a + "  1:" + (int)'9' + "i"  + " 0 :" + (int)'0');
    }
}
