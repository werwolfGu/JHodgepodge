package com.guce;

import java.util.HashMap;
import java.util.Map;

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

    public static String solution(String s){
        int max = 0 ;
        int idx = 0 ;


        Map<Character,Integer> map = new HashMap<>();

        for(int i = 0 ; i < s.length() ; i++ ){
            Character ch = s.charAt(i);
            if(map.containsKey(ch)){
                int value = map.get(ch);
                int v1 = i - value;
                if(max < v1 && value >= idx){

                    idx = value;
                    max = v1;
                }

            }
            map.put(ch,i);
        }
        if(max > 0 ){
            return s.substring(idx,idx + max + 1 );
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(solution("cbbd"));
        char i = '1';
        char a = 'a';
        if(i > 48 && i < 57)
        System.out.println("a " + (int)a + "  1:" + (int)'9' + "i"  + " 0 :" + (int)'0');
    }
}
