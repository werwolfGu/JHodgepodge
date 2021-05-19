package com.guce.dp;

import java.util.Arrays;

/**
 * @Author chengen.gce
 * @DATE 2020/9/15 8:40 下午
 * https://leetcode-cn.com/problems/longest-increasing-subsequence/
 * 最长上升子序列
 */
public class LengthOfLIS {

    public static int lengthOfLIS(int[] nums) {

        int len = nums.length;
        if (len < 2) {
            return len;
        }

        int[] dp = new int[len];
        Arrays.fill(dp, 1);
        int res = 1 ;

        for (int i = 1 ; i < nums.length ; i++ ){
            for (int j = 0 ; j < i ; j++ ){
                if (nums[j] < nums[i]){
                    dp[i] = Math.max(dp[i],dp[j] + 1);
                }
            }
            res = Math.max(res,dp[i]);
        }

        return res;
    }

    /**
     * 维护一个单调递增序列
     * @param nums
     * @return
     */
    public static int lengthOfLISV1(int[] nums) {

        if (nums.length < 2){
            return nums.length ;
        }
        int[] tails = new int[nums.length];
        int res = 0;
        int pos = 0;
        for (int i = 0 ; i < nums.length ; i++ ){

            if (tails[pos] > nums[i]){
                tails[pos] = nums[i];
                continue;
            }
            int l = 0 , r = pos;
            while (l < r){
                int mid = (l +r) >> 2;
                if (tails[mid] < nums[i]){
                    l = mid + 1;
                }else {
                    r = mid;
                }
            }
            tails[l] = nums[i];
            if (r == pos) pos++ ;
        }
        return pos ;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{4,10,4,3,8,9};
        System.out.println(lengthOfLIS(nums));
        System.out.println(lengthOfLISV1(nums));
    }
}
