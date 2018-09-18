package com.guce;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chengen.gu on 2018/9/18.
 *
 * 给定一个整数数组和一个目标值，找出数组中和为目标值的两个数。

 * 你可以假设每个输入只对应一种答案，且同样的元素不能被重复利用。

 * 示例:
 * 给定 nums = [2, 7, 11, 15], target = 9
 * 因为 nums[0] + nums[1] = 2 + 7 = 9
 * 所以返回 [0, 1]
 */
public class TwoSum {

    public int[] solution(int[] nums,int target){
        int result[] = new int[2];

        Map<Integer,Integer> map = new HashMap<>();
        for(int i = 0 ; i< nums.length ; i++ ){

            int currNum = target - nums[i];
            if(map.containsKey(currNum)){
                result[0] = map.get(currNum);
                result[1] = i;
                break;
            }
            map.put(nums[i],i);
        }
        return result;
    }

    public static void main(String[] args) {
        TwoSum sum = new TwoSum();
        int[] nums = new int[]{2, 7, 11, 15};
        int target = 9;
        int result[] = sum.solution(nums,target);
        System.out.println("" + result[0] + "    " + result[1]);
    }
}
