package com.guce.dp;

/**
 * @Author chengen.gce
 * @DATE 2020/11/28 12:12 上午
 * https://leetcode-cn.com/problems/house-robber-ii/
 *
 * 你是一个专业的小偷，计划偷窃沿街的房屋，每间房内都藏有一定的现金。这个地方所有的房屋都 围成一圈 ，这意味着第一个房屋和最后一个房屋是紧挨着的。同时，相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警 。
 *
 * 给定一个代表每个房屋存放金额的非负整数数组，计算你 在不触动警报装置的情况下 ，能够偷窃到的最高金额。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：nums = [2,3,2]
 * 输出：3
 * 解释：你不能先偷窃 1 号房屋（金额 = 2），然后偷窃 3 号房屋（金额 = 2）, 因为他们是相邻的。
 * 示例 2：
 *
 * 输入：nums = [1,2,3,1]
 * 输出：4
 * 解释：你可以先偷窃 1 号房屋（金额 = 1），然后偷窃 3 号房屋（金额 = 3）。
 *      偷窃到的最高金额 = 1 + 3 = 4 。
 */
public class Rob {

    public int rob(int nums[]){

        if (nums == null || nums.length == 0){
            return 0;
        }
        if (nums.length == 1){
            return nums[0];
        }
        int len = nums.length;
        int dp[] = new int[len];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0],nums[1]);
        for (int i = 2 ; i < len - 1 ;i++){
            dp[i] = Math.max(dp[i -1],nums[i] + dp[i - 2]);
        }
        int max0 = dp[len - 2];
        if (nums.length == 2){
            return Math.max(nums[0],nums[1]);
        }
        int[] dp1 = new int[len + 1];
        dp1[1] = nums[1];
        dp1[2] = Math.max(nums[1],nums[2]);
        for (int i = 3 ; i < len ; i++ ){
            dp1[i] = Math.max(dp1[i -1],nums[i] + dp1[i - 2]);
        }
        int max1 = dp1[len -1];
        return Math.max(max0,max1);
    }
}
