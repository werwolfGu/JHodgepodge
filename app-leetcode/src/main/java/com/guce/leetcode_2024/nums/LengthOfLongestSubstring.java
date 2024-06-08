package com.guce.leetcode_2024.nums;

/**
 * @Author chengen.gce
 * @DATE 2024/3/11 22:09
 */
public class LengthOfLongestSubstring {

    public int lengthOfLongestSubstring(String s) {
        int[] repeat = new int[26];
        if (s == null || s.length()<= 0 ){
            return 0;
        }
        int ans = 0;
        for (int i = 0 ; i < s.length() ; i++) {
            char ch = s.charAt(i);
            int idx = ch - 'a';
            if (repeat[idx] == 0) {
                repeat[idx] = 1;
                ans++;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        LengthOfLongestSubstring lengthOfLongestSubstring = new LengthOfLongestSubstring();
        int count = lengthOfLongestSubstring.lengthOfLongestSubstring("pwwkew");
        System.out.println(count);
    }
}
