package com.guce;

import java.util.Arrays;
import java.util.List;

/**
 * @Author chengen.gu
 * @DATE 2019/10/29 10:15 下午
 * https://leetcode-cn.com/problems/word-break/solution/dong-tai-gui-hua-jing-dian-de-wan-quan-bei-bao-w-3/
 */
public class WordBreak {
    public static boolean soluton(String str , List<String> wordDict){

        if (str == null || "".equals(str)){
            return false;
        }
        if (wordDict == null || wordDict.size() == 0){
            return false;
        }
        boolean dp[] = new boolean[str.length() + 1];
        dp[0] = true;
        for (int i = 1 ; i <=str.length() ; i++ ){
            for (int j = 0 ; j < i ;j++ ){
                String tmp = str.substring(j,i);
                if (dp[j] && wordDict.contains(tmp)){
                    dp[i] = true;
                    break;
                }
            }
        }
        for ( int i = 0 ; i < dp.length ; i++){
            System.out.print(dp[i] + " ");
        }
        return dp[str.length()];
    }

    public static void main(String[] args) {
        System.out.println("\n result:" + WordBreak.soluton("leetcode", Arrays.asList("leet","code")));
    }
}
