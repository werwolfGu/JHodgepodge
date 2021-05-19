package com.guce;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author chengen.gce
 * @DATE 2021/2/27 3:20 下午
 *
 * https://leetcode-cn.com/problems/degree-of-an-array/
 *
 *
 * 给定一个非空且只包含非负数的整数数组 nums，数组的度的定义是指数组里任一元素出现频数的最大值。
 *
 * 你的任务是在 nums 中找到与 nums 拥有相同大小的度的最短连续子数组，返回其长度。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：[1, 2, 2, 3, 1]
 * 输出：2
 * 解释：
 * 输入数组的度是2，因为元素1和2的出现频数最大，均为2.
 * 连续子数组里面拥有相同度的有如下所示:
 * [1, 2, 2, 3, 1], [1, 2, 2, 3], [2, 2, 3, 1], [1, 2, 2], [2, 2, 3], [2, 2]
 * 最短连续子数组[2, 2]的长度为2，所以返回2.
 * 示例 2：
 *
 * 输入：[1,2,2,3,1,4,2]
 * 输出：6
 */
public class FindShortestSubArray {

    public static int findShortestSubArray(int[] nums) {
        //  int[] 有3列  0 : 度的大小 ； 1： 起始位置 ；2：最后位置
        Map<Integer, int[]> map = new HashMap<>();

        for (int i = 0 ; i < nums.length ; i++ ){

            int arr[] = map.get(nums[i]);
            if (arr != null){
                arr[0]++ ;
                arr[2] = i;
            }else {
                arr = new int[]{1,i,i};
                map.put(nums[i],arr);
            }
        }

        int minLen = nums.length ,maxNums = 0;
        for (Map.Entry<Integer,int[]> entry : map.entrySet()){
            int arr[] = entry.getValue();
            if (maxNums < arr[0]){
                maxNums = arr[0];
                minLen = arr[2] - arr[1] + 1;
            }else if (maxNums == arr[0]){
                int len = arr[2] - arr[1] + 1;
                if (minLen > len){

                    minLen = len;
                }
            }
        }
        return minLen;
    }

    public static void main(String[] args) {

        System.out.println(findShortestSubArray(new int[]{1,2,2,3,1,4,2}));
    }
}
