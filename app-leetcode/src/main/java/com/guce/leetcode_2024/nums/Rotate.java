package com.guce.leetcode_2024.nums;

/**
 * @Author chengen.gce
 * @DATE 2024/3/3 15:21
 */
public class Rotate {
    public void rotate(int[] nums, int k) {
        int n = nums.length;
        int[] newArr = new int[n];
        for (int i = 0; i < n; ++i) {
            newArr[(i + k) % n] = nums[i];
        }
        System.arraycopy(newArr, 0, nums, 0, n);

    }

    public static boolean isSubsequence(String s, String t) {

        int tIdx = 0 ;
        for (int i = 0 ; i < t.length() ; i++) {
            char tCh =  s.charAt(tIdx);
            char sCh = t.charAt(i);
            if (tCh == sCh) {
                tIdx++;
            }
        }
        if (tIdx == s.length() - 1) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        isSubsequence("b", "abc");
    }
}
