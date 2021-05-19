package com.guce;

import java.util.Arrays;

/**
 * @Author chengen.gce
 * @DATE 2020/9/11 11:41 下午
 */
public class MergeNum {

    public void merge(int[] nums1,int m , int[] nums2,int n){
        int idx = 0 ;
        for(int i = m ; i < m +n ; i++){
            nums1[i] = nums2[idx++];
        }
        Arrays.sort(nums1);
    }


    public static void main(String[] args) {

        MergeNum mergeNum = new MergeNum();

        int[] nums1 = new int[]{1,2,3,0,0,0};
        int[] nums2 = new int[]{2,5,6};
        mergeNum.merge(nums1,3,nums2,3);

        for (int i : nums1){
            System.out.print(i + " ");
        }
    }
}
