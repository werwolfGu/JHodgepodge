package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2021/3/25 9:34 上午
 * d第一个缺少的正数
 */
public class FirstMissingPositive {

    public int firstMissingPositive(int[] nums) {

        ////将 <= 0 的数 改成 n+1
        int n = nums.length;
        for (int i = 0 ; i < n ; i++){
            if (nums[i] <= 0){
                nums[i] = n + 1;
            }
        }

        ///// 将 < n 的数修改成下表为nums[i]对应的负数
        for (int i = 0 ; i < n ; i++){
            if (Math.abs(nums[i]) < n){
                nums[nums[i] - 1] = - Math.abs(nums[nums[i] - 1]);
            }
        }

        ////找到第一个 < n 且为正数的下标
        for (int i = 0 ; i < n ; i++ ){
            if (nums[i] > 0){
                return i + 1;
            }
        }
        return n + 1;
    }
}
