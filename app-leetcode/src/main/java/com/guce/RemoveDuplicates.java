package com.guce;

/**
 * @Author chengen.gu
 * @DATE 2019/10/26 3:49 下午
 */
public class RemoveDuplicates {

    public int soution(int nums[]) {
        if (nums == null) {
            return 0;
        }
        if (nums.length < 2) {
            return 1;
        }
        int len = nums.length, i = 0, j = 1;

        int repeatNum = 0;
        int idx = 0;
        while (idx++ < nums.length) {
            if (nums[i] == nums[j]) {
                repeatNum++;
                moveNums(nums, j, len - repeatNum + 1);
            } else {
                j++;
                i++;
            }
        }
        System.out.println("repeat :" + repeatNum);
        return repeatNum - 1;
    }

    public void moveNums(int[] nums, int begin, int len) {

        for (int i = begin; i < len - 1; i++) {

            nums[i] = nums[i + 1];
        }

    }

    public int removeDuplicate(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                i++;
                nums[i] = nums[j];
            }
        }
        return i + 1;
    }


    public static void main(String[] args) {
        RemoveDuplicates duplicates = new RemoveDuplicates();
        int[] nums = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        int x = duplicates.soution(nums);
        System.out.println(x);
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i] + " ");
        }
    }
}
