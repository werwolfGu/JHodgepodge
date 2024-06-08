package com.guce.leetcode_2024.nums;

/**
 * @Author chengen.gce
 * @DATE 2024/3/2 22:42
 */
public class RemoveElement {
    public int removeElement(int[] nums, int val) {

        int result = 0 ;
        int left = 0 , right = nums.length - 1 ;
        while (left < right) {
            //从左边找第一个 nums[i] == val;
            boolean flag = false;
            if (nums[left] == val) {
                flag = true;
                result++;
                left++;
            }
            while (flag && (right >= left)) {
                if (nums[right--] == val) {
                    result++;
                } else {
                    nums[left -1] = nums[right+1];
                    nums[right+1] = val;
                    break;
                }
            }
            if (!flag) {
                left++;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1};
        int val = 1;
        System.out.println(new RemoveElement().removeElement(nums, val));
    }
}
