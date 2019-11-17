package com.guce;

import java.util.Arrays;
import java.util.Collections;

/**
 * @Author chengen.gu
 * @DATE 2019/11/13 10:21 下午
 * https://leetcode-cn.com/problems/next-permutation/
 */
public class NextPermutation {

    public static void solution(Integer nums[]){

        int i = nums.length - 2;
        while (i >= 0 && nums[i + 1] <= nums[i]) {
            i--;
        }
        if (i >= 0) {
            int j = nums.length - 1;
            while (j >= 0 && nums[j] <= nums[i]) {
                j--;
            }
            swap(nums, i, j);
        }
        reverse(nums, i + 1);
    }

    private static void reverse(Integer[] nums, int start) {
        int i = start, j = nums.length - 1;
        while (i < j) {
            swap(nums, i, j);
            i++;
            j--;
        }
    }

    private static void swap(Integer[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    public static void main(String[] args) {
        Integer nums[] = {3,2,1};

        solution(nums);

        //sort(nums);
        Arrays.sort(nums, Collections.reverseOrder());
        for ( int i = 0 ; i < nums.length ;i++){
            System.out.println(nums[i]);
        }
    }
}
