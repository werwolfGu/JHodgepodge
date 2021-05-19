package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/6/13 1:37 下午
 * https://leetcode-cn.com/problems/find-first-and-last-position-of-element-in-sorted-array/
 * <p>
 * 给定一个按照升序排列的整数数组 nums，和一个目标值 target。找出给定目标值在数组中的开始位置和结束位置。
 * <p>
 * 示例 1:
 * <p>
 * 输入: nums = [5,7,7,8,8,10], target = 8
 * 输出: [3,4]
 * 示例 2:
 * <p>
 * 输入: nums = [5,7,7,8,8,10], target = 6
 * 输出: [-1,-1]
 */
public class SearchRange {

    public static int[] solution(int[] nums,int target){
        int l = 0 , r = nums.length - 1;
        int[] result = new int[]{-1,-1};
        while (l < r){
            int middle = (l +r)/2;
            if (nums[middle] < target){
                l = middle + 1;
                continue;
            }
            if (nums[middle] > target){
                r = middle -1;
                continue;
            }
            //找到左边最小的数
            int num1 = middle;
            while (num1 >= 0 && nums[num1] == target){
                result[0] = num1--;
            }

            int num2 = middle;
            while (num2 < nums.length && nums[num2] == target){
                result[1] = num2++;
            }

            break;
        }
        return result;
    }

    public static int[] solution1(int[] nums,int target){

        int res[] = new int[2], l = 0 , r = nums.length - 1;

        while ( l <= r){
            if (nums[l++] == target){
                res[0] = l - 1;
                break;
            }
        }
        while (l <= r){
            if (nums[r--] == target ){
                res[1] = r + 1;
                break;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] res = solution(new int[]{5,7,7,8,8,10},8);

        for (int i = 0 ; i < res.length ; i++){
            System.out.println(res[i]);
        }

        res = solution1(new int[]{5,7,7,8,8,10},8);

        for (int i = 0 ; i < res.length ; i++){
            System.out.println(res[i]);
        }

    }
}
