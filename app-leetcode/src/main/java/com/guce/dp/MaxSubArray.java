package com.guce.dp;

/**
 * @Author chengen.gu
 * @DATE 2019/11/17 6:52 下午
 * https://leetcode-cn.com/problems/maximum-subarray/
 * 求最大子序列的和；且这个子序列是连续的
 *
 * 输入: [-2,1,-3,4,-1,2,1,-5,4],
 * 输出: 6
 * 解释: 连续子数组 [4,-1,2,1] 的和最大，为 6。
 */
public class MaxSubArray {

    public static int solution(int nums[]){
       int ans = nums[0],sum = 0 ;
        for (int i = 0 ; i < nums.length ; i++ ){
            if ( sum > 0){
                sum += nums[i];
            }else {
                sum = nums[i];
            }
            ans = Math.max(ans,sum);
        }
        return ans;
    }

    public static void main(String[] args) {

        System.out.println(solution(new int[]{1,2,3,5}));
    }
}
