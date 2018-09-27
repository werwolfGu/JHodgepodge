package com.guce;

/**
 * Created by chengen.gu on 2018/9/27.
 */
public class SortCollections {

    /**
     * 快速排序
     * @param nums
     * @param begin
     * @param end
     */
    public static void quickSort(int[] nums,int begin,int end){

        int left = begin + 1,right = end,middle = begin;

        int sortValue = nums[begin];

        while(left < right){

            //从右边开始找小于当前值的数
            while (sortValue < nums[right] && left < right) right--;
            if(left < right){
                nums[middle] = nums[right];
                middle = right;
            }

            //从左边开始找大于当前值的数
            while(sortValue >= nums[left]  && left < right) left++ ;

            if(left < right){
                nums[middle] = nums[left];
                middle = left;
            }

        }
        nums[middle] = sortValue;

        if(begin < middle){
            quickSort(nums,begin,middle -1);
        }
        if(middle < end){
            quickSort(nums,middle+1,end);
        }

    }

    public static void main(String[] args) {

        int[] nums = new int[]{5,3,2,-1, 0, 1, 2,3, -1, -4,2,3,5};
        SortCollections.quickSort(nums,0,nums.length -1 );
        for(int i = 0 ; i < nums.length; i++){
            System.out.println(nums[i]);
        }
    }
}
