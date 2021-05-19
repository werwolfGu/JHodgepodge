package com.guce.dp;

/**
 * @Author chengen.gce
 * @DATE 2020/3/24 8:59 下午
 * https://leetcode-cn.com/problems/the-masseuse-lcci/
 * 一个有名的按摩师会收到源源不断的预约请求，每个预约都可以选择接或不接。在每次预约服务之间要有休息时间，因此她不能接受相邻的预约。给定一个预约请求序列，替按摩师找到最优的预约集合（总预约时间最长），返回总的分钟数。
 */
public class Massage {

    public static int massage(int[] nums) {
        int[][] dp = new int[nums.length + 1][2];
        dp[0][0] = 0;
        dp[0][1] = nums[0];
        // dp[i][0]：区间 [0, i) 里接受预约请求，并且下标为 i 的这一天不接受预约的最大时长
        // dp[i][1]：区间 [0, i) 里接受预约请求，并且下标为 i 的这一天接受预约的最大时长
        for (int i = 1; i <= nums.length; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][1]);
            dp[i][1] = dp[i - 1][0] + nums[i - 1];
        }
        return Math.max(dp[nums.length][0], dp[nums.length][1]);
    }

    public static int massage1(int nums[]) {
        if (nums.length == 1) {
            return nums[0];
        }
        int dp[] = new int[nums.length];

        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);

        for (int i = 2; i < nums.length; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }

        return dp[nums.length - 1];
    }

    public static void main(String[] args) {
        int nums[] = new int[]{2, 1, 4, 5, 3, 1, 1, 3};
        System.out.println(massage(nums));
        System.out.println(massage1(nums));
    }
}
