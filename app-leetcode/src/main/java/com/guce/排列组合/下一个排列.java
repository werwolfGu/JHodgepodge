package com.guce.排列组合;

/**
 * @Author chengen.gce
 * @DATE 2021/5/23 2:53 下午
 * https://leetcode-cn.com/problems/next-permutation/
 */
public class 下一个排列 {

    public void nextPermutation(int[] nums) {
        // 1. 从后向前找到第一个不是倒序的元素
        int len = nums.length;
        int i = len - 2;
        while (i >= 0 && nums[i] >= nums[i + 1]){
            i--;
        }

        //2. 从 倒序排序的元素(i , len ) 中找到第一个大于 nums[i] 的元素 并交换
        if ( i >= 0){
            int k = len - 1;
            while (k >= i && nums[i] >= nums[k]){
                k--;
            }
            swap(nums,i,k);
        }

        /////将 i+1 到 len - 1 和之间的元素进行交换
        int left = i + 1, right = len -1;
        while (left < right){
            swap(nums,left,right);
            left++ ;
            right-- ;
        }
    }

    public void swap(int nums[] ,int l , int r){
        int x = nums[l];
        nums[l] = nums[r];
        nums[r] = x;
    }
}
