package com.guce.dp;

/**
 * @Author chengen.gce
 * @DATE 2021/3/21 8:25 下午
 *
 * https://leetcode-cn.com/problems/target-sum/
 *
 * 给定一个非负整数数组，a1, a2, ..., an, 和一个目标数，S。现在你有两个符号 + 和 -。对于数组中的任意一个整数，你都可以从 + 或 -中选择一个符号添加在前面。
 *
 * 返回可以使最终数组和为目标数 S 的所有添加符号的方法数。
 *
 *  
 *
 * 示例：
 *
 * 输入：nums: [1, 1, 1, 1, 1], S: 3
 * 输出：5
 * 解释：
 *
 * -1+1+1+1+1 = 3
 * +1-1+1+1+1 = 3
 * +1+1-1+1+1 = 3
 * +1+1+1-1+1 = 3
 * +1+1+1+1-1 = 3
 *
 * 一共有5种方法让最终目标和为3
 */
public class FindTargetSumWays {

    int count = 0;
    public int findTargetSumWays(int[] nums, int S) {

        calculate(nums,0,0,S);
        return count;
    }

    /**
     * 回溯法
     * @param nums
     * @param idx
     * @param sum
     * @param S
     */
    public void calculate(int[] nums ,int idx , int sum ,int S){
        if (idx == nums.length){
            if (sum == S){
                count++ ;
            }
        }else {
            calculate(nums,idx + 1 ,sum + nums[idx],S);
            calculate(nums,idx + 1 ,sum - nums[idx],S);
        }
    }

    public static void main(String[] args) {

        FindTargetSumWays sumWays = new FindTargetSumWays();
        System.out.println(sumWays.findTargetSumWays(new int[]{1, 1, 1, 1, 1},3));
    }
}
