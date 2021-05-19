package com.guce.backtrace;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2020/9/10 9:24 下午
 * 使用回溯法会超时
 */
public class ThreeSum {

    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);

        List<List<Integer>> result = new ArrayList<>();
        ArrayDeque deque = new ArrayDeque();
        boolean[] used = new boolean[nums.length];
        backtrace(nums,deque,0,0,used,result);
        return result;
    }

    public void backtrace(int[] nums,Deque<Integer> deque ,int begin, int sum ,boolean[] used, List<List<Integer>> result ){

        if (deque.size() == 3 ){
            if (sum == 0){
                result.add(new ArrayList<>(deque));
            }
            return;
        }
        for (int i = begin ; i < nums.length ; i++ ){
            if (sum - nums[i] < 0){
                return ;
            }
            if (used[i]) {
                continue;
            }
            if (i > 0 && nums[i -1] == nums[i] && !used[i - 1 ]){
                continue;
            }
            deque.add(nums[i]);
            used[i] = true;
            backtrace(nums,deque,i + 1 , sum - nums[i] ,used ,result);
            used[i] = false;
            deque.removeLast();
        }
    }

    public static void main(String[] args) {
        int[] nums = new int[]{-1, 0, 1, 2, -1, -4,4,-3};

        ThreeSum threeSum = new ThreeSum();
        List<List<Integer>> result  = threeSum. threeSum(nums);
        System.out.println(result);
    }
}
