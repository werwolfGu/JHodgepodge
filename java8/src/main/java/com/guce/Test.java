package com.guce;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2024/7/3 13:04
 */
public class Test {

    public List<List<Integer>> candidates(int[] nums , int target) {

        Deque<Integer> stack = new ArrayDeque<>();
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(nums);
        backTrace(nums,0,target,stack,res);
        return res;
    }

    public void backTrace(int[] nums  , int idx , int target , Deque<Integer> stack , List<List<Integer>> res) {
        if (target == 0) {
            res.add(new ArrayList<>(stack));
            return ;
        }
        for (int i = idx ; i < nums.length ; i++) {
            if (nums[i] > target) {
                break;
            }
            stack.addLast(nums[i]);
            backTrace(nums,i , target - nums[i] ,stack,res);
            stack.removeLast();
        }

    }

    public static void main(String[] args) {
        Test t = new Test();
        int[] nums = {2,3,6,7};
        List<List<Integer>> res = t.candidates(nums,7);
        System.out.println(res);
        nums = new int[]{2,3,5};
        res = t.candidates(nums,8);
        System.out.println(res);
    }
}
