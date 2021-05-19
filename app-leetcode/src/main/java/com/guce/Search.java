package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/6/11 10:17 下午
 */
public class Search {

    public static int solution(int nums[] ,int target){
        int l = 0 , r = nums.length - 1;
        while( l <= r){
            int mid = (l + r) /2;
            if (nums[mid] == target ){
                return mid;
            }
            if (nums[mid ] > target){
                r = mid - 1;
            }else {
                l = mid + 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(solution(new int[]{-1,0,3,5,9,12},2));
    }
}
