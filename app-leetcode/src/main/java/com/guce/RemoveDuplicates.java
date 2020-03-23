package com.guce;

/**
 * @Author chengen.gu
 * @DATE 2019/10/26 3:49 下午
 * https://leetcode-cn.com/problems/remove-duplicates-from-sorted-array-ii/submissions/
 */
public class RemoveDuplicates {

    public int soution(int nums[]) {
        if (nums == null || nums.length < 2) {
            return 1;
        }
        int idx = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[idx]) {
                if (i - idx > 1) {
                    nums[idx + 1] = nums[i];
                }
                idx++;
            }
        }
        return idx + 1;
    }

    public int removeDuplicate(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                if ( i != j){
                    nums[i + 1] = nums[j];
                }

                i++;
            }
        }
        return i + 1;
    }

    public static int removeDuplicates2(int nums[]) {

        if (nums == null) {
            return 0;
        }
        if (nums.length < 3) {
            return nums.length;
        }
        int len = 0;
        int idx = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[idx] != nums[i]) {
                if (i - idx > 1) {
                    nums[idx + 2] = nums[i];
                }
                idx++;
            }
        }
        return idx + 2;
    }


    public static void main(String[] args) {
        RemoveDuplicates duplicates = new RemoveDuplicates();
        int[] nums = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4, 5};
        int x = duplicates.soution(nums);
        System.out.println(x);
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i] + " ");
        }
        System.out.println("");
        nums = new int[]{1, 1, 1, 2, 2, 3};
        System.out.println(removeDuplicates2(nums));
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i] + " ");
        }
        System.out.println("");
        nums = new int[]{0, 0, 1, 1, 1, 1, 2, 3, 3};
        System.out.println(removeDuplicates2(nums));
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i] + " ");
        }

    }
}
