package com.guce;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by chengen.gu on 2018/9/18.
 * 给定一个字符串，找出不含有重复字符的最长子串的长度。
 *
 *   示例 1:
 输入: "abcabcbb"
 输出: 3
 解释: 无重复字符的最长子串是 "abc"，其长度为 3。

 示例 2:
 输入: "bbbbb"
 输出: 1
 解释: 无重复字符的最长子串是 "b"，其长度为 1。

 示例 3:
 输入: "pwwkew"
 输出: 3
 解释: 无重复字符的最长子串是 "wke"，其长度为 3。
 请注意，答案必须是一个子串，"pwke" 是一个子序列 而不是子串。
 *
 *
 */
public class LengthOfLongestSubstring {

    /**
     * 解决思路：
     * @param s
     * @return
     */
    public static int lengthOfLongestSubstring(String s){

        if(s == null || "".equals(s)){
            return 0;
        }
        int n = s.length();
        int i= 0,j = 0 , maxLen = 0 ;

        Set<Character> set = new HashSet<>();

        while(i < n && j < n){

            Character ch = s.charAt(j);

            if(!set.contains(ch)){
                j++;
                maxLen = Math.max(maxLen,j - i);
                set.add(ch);
            }else{
                set.remove(s.charAt(i++));
            }
        }
        return maxLen ;
    }

    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring("sdaddpwen"));
    }
}
