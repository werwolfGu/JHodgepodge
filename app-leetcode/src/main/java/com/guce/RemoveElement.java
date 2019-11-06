package com.guce;

/**
 * @Author chengen.gu
 * @DATE 2019/10/29 9:21 下午
 */
public class RemoveElement {

    public static int solution(int nums[], int val) {
        int valIdx = 0;

        for (int i = 0; i < nums.length; i++) {

            if (nums[i] != val) {
                nums[valIdx] = nums[i];
                valIdx++;
            }
        }
        return valIdx;
    }

    public static void main(String[] args) {
        int nums[] = {0, 1, 2, 2, 3, 0, 4, 2};
        int idx = RemoveElement.solution(nums, 2);
        System.out.println("idx: " + idx);

        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i] + " ");

        }
    }
}
