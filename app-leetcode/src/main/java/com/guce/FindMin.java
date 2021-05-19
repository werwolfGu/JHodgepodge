package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2021/4/18 3:18 下午
 */
public class FindMin {

    public int findMin(int[] nums){

        int l = 0 , r = nums.length - 1;
        while ( l < r){

            int mid = ( l + r) / 2;
            if (nums[mid] < nums[r]){
                r = mid ;
            }else {
                l = mid + 1;
            }
        }
        return nums[l];
    }


    public int findMax(int[] nums){

        int l = 0 , r = nums.length - 1;
        while ( l < r){

            int mid = ( l + r) / 2;
            if (nums[mid] < nums[l]){
                r = mid - 1;
            }else {
                l = mid;
            }
        }
        return nums[l];
    }

    public static void main(String[] args) {
        FindMin findMin = new FindMin();
        System.out.println(findMin.findMin(new int []{3,4,5,1,2}));
        System.out.println(findMin.findMax(new int []{3,4,5,1,2}));
    }
}
