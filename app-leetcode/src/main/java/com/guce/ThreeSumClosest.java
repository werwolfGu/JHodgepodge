package com.guce;

import java.util.Arrays;

/**
 * Created by chengen.gu on 2018/9/27.
 * https://leetcode-cn.com/problems/3sum-closest/
 * 最接近的3数之和
 */
public class ThreeSumClosest {

    public static int solution(int[] nums, int target) {

        Arrays.sort(nums);
        int minClosest = 1000000;
        for (int i = 0; i < nums.length; i++) {

            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int j = i + 1, k = nums.length - 1;
            while (j < k) {


                int sum = nums[i] + nums[j] + nums[k];

                if (sum == target) {
                    return sum;
                }
                if (Math.abs(sum - target) < Math.abs(minClosest - target)) {
                    minClosest = sum;
                }

                if (sum > target) {
                    k--;
                    while (j < k && nums[k] == nums[k + 1]) {
                        k--;
                    }
                    continue;
                }

                if (sum < target) {
                    j++;
                    while (j < k && nums[j] == nums[j - 1]) {
                        j++;
                    }

                }


            }
        }
        return minClosest;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{-3, -2, -5, 3, -4};

        System.out.println(solution(nums, -1));
    }
}
