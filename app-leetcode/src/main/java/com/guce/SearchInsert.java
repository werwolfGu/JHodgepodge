package com.guce;

/**
 * @Author chengen.gu
 * @DATE 2019/11/17 6:13 下午
 */
public class SearchInsert {

    /**
     * 二分查找法
     * @param nums
     * @param target
     * @return
     */
    public static int solution(int nums[] ,int target){

        if (nums == null || nums.length == 0){
            return -1 ;
        }
        int left = 0 ,right = nums.length -1;
        while (left < right){
            int mid = (left + right) >>> 1;
            if (nums[mid] == target){
                return mid;
            }
            if ( nums[mid] < target){
                left = mid + 1;
            }else {
                right = mid -1 ;
            }

        }
        return left;
    }

    /**
     * 顺序查找法
     * @param nums
     * @param target
     * @return
     */
    public static int sequentialSearch(int nums[] ,int target){

        int i = 0 ;
        for ( ; i < nums.length ; i++ ){
            if (nums[i] == target || target < nums[i]){
                return i;
            }
        }
        if ( nums.length == i){
            return i;
        }
        return 0;
    }

    public static void main(String[] args) {
        int[] nums = {1,3,5,6};

        System.out.println(solution(nums,2));

        int i = -20 ;
        System.out.println(i >> 2);
        System.out.println( i>>> 2);
    }
}
