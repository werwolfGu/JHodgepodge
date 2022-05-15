package com.guce;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2020/5/19 3:26 下午  解码方法
 * a-z分别对应数字1-26, 给你一串数字(不能改变顺序), 方法得出:一串数字解码成对应的英文字母, 有几种可能性?
 * https://leetcode-cn.com/problems/decode-ways/
 */
public class TouTiaoInterview {

    private static int count = 0 ;
    public static int solution(int[] nums){
        int[] dp = new int[nums.length + 1];
        if (nums[0] == 0){
            return 0;
        }
        dp[0] = 1;
        dp[1] = nums[0] == 0 ? 0 : 1;
        for (int i = 2 ; i <= nums.length ; i++){
            int x = nums[i - 1];
            int y = nums[i - 2];
            if ( (y == 1 || y == 2 ) && x < 7){
                if (x == 0){
                    dp[i] = dp[ i -2 ];
                }else {
                    dp[i] = dp[i -1] + dp[i -2];
                }
            }else if (y == 0){
                return 0;

            }else {
                dp[i] = dp[i -1] ;
            }
        }
        return dp[nums.length];
    }

    public static int solution(String  s){
        List<Integer> list = new ArrayList<>();

        if (s.charAt(0)=='0'){
            return 0;
        }
        for (int i = 0 ; i < s.length() ;i++) {
            char ch = s.charAt(i);
            list.add(ch - '0');
        }


        int[] dp = new int[list.size() + 1];
        dp[0] = 1;
        dp[1] = list.get(0) == 0 ? 0 : 1;
        for (int i = 2 ; i <= list.size() ; i++ ){
            int a = list.get(i-1 );
            int b = list.get(i -2);


        }
        return dp[list.size() ];
    }

    public static int solution1(String s){
        int[] dp = new int[s.length() + 1];

        dp[0] = 1;
        dp[1] = s.charAt(0) == '0' ? 0 : 1;
        for (int i = 2 ; i <= s.length() ; i++){

            if ( s.charAt(i - 2) == '1' || s.charAt(i -2) == '2' && s.charAt(i -1) < '7' ){
                // 10 20
                if (s.charAt(i-1) == '0'){
                    dp[i] = dp[i - 2];
                }else {
                    dp[i ] = dp[i -2] + dp[i - 1];
                }
            }else if (s.charAt(i - 1) == '0'){
                return 0;
            }else {
                dp[i] = dp[i -1];
            }

        }
        return dp[s.length()];
    }

    public static void main(String[] args) {
        System.out.println(solution(new int[]{1,3,0}));
        //System.out.println(solution("2611055971756562"));
        System.out.println(solution1("2611055971756562"));

        String s = "28";
        if (s.charAt(0) == '1' || s.charAt(0) == '2' && s.charAt(1) < '7'){
            System.out.println(s);
        }
    }

}
