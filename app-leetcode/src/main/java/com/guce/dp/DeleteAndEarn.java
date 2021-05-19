package com.guce.dp;

/**
 * @Author chengen.gce
 * @DATE 2021/3/29 12:16 下午
 *
 * https://leetcode-cn.com/problems/delete-and-earn/
 *
 * 给定一个整数数组 nums ，你可以对它进行一些操作。
 *
 * 每次操作中，选择任意一个 nums[i] ，删除它并获得 nums[i] 的点数。之后，你必须删除每个等于 nums[i] - 1 或 nums[i] + 1 的元素。
 *
 * 开始你拥有 0 个点数。返回你能通过这些操作获得的最大点数。
 *
 * 示例 1:
 *
 * 输入: nums = [3, 4, 2]
 * 输出: 6
 * 解释:
 * 删除 4 来获得 4 个点数，因此 3 也被删除。
 * 之后，删除 2 来获得 2 个点数。总共获得 6 个点数。
 *
 * nums的长度最大为20000。
 * 每个整数nums[i]的大小都在[1, 10000]范围内。
 */
public class DeleteAndEarn {

    public int deleteAndEarn(int[] nums) {
        int[] count = new int[10001];
        for (int i = 0 ; i < nums.length ;i++ ){
            count[nums[i]]++;
        }
        int[] dp = new int[10001];
        dp[0] = count[0];
        dp[1] = Math.max(count[0],count[1]);
        for (int i = 2 ; i <= 10000 ; i++){
            dp[i] = Math.max(dp[i -1] , dp[i -2] + (count[i] * i));
        }
        return dp[10000];
    }
}
