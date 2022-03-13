package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2021/7/13 11:25 下午
 */
public class SortedSquares {

    public int[] sortedSquares(int[] nums){
        if (nums== null ){
            return nums;
        }
        int len = nums.length;
        int[] ans = new int[len];
        int split = -1 ;
        for (int i = 0 ; i < len ; i++){
            if (nums[i] < 0){
                split++;
            }else {
                break;
            }
        }
        int idx = 0  , left = split , right = split + 1;
        while (left >= 0  || right < len){
            if (left < 0 ){
                ans[idx++] = ans[right] * ans[right];
                right++ ;
            } else if (right >= len ){
                ans[idx++] = nums[left] * nums[left];
                left--;
            } else if (nums[left] * nums[left] < nums[right] * nums[right]){
                ans[idx++] = nums[left] * nums[left];
                left--;
            } else {
                ans[idx++] = nums[right] * nums[right];
                right++ ;
            }
        }
        return ans;
    }

    public static void main(String[] args) {


        SortedSquares sortedSquares = new SortedSquares();
        sortedSquares.sortedSquares(new int[]{-4,-1,0,3,10});
    }
}
