package com.guce.组合;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2021/5/16 10:02 下午
 */
public class 组合总和 {

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();

        Deque<Integer> list = new ArrayDeque<>();
        Arrays.sort(candidates);
        dfs(candidates, 0, 0, target, list, result);
        return result;
    }

    public void dfs(int nums[], int begin, int sum, int target, Deque<Integer> stack, List<List<Integer>> result) {
        if (sum == target) {
            result.add(new ArrayList<>(stack));
            return;
        }

        for (int i = begin; i < nums.length; i++) {
            if (sum + nums[i] > target) {
                break;
            }
            stack.addLast(nums[i]);
            dfs(nums, i + 1, sum + nums[i], target, stack, result);
            stack.removeLast();
        }

    }

    public static void main(String[] args) {
        组合总和 instance = new 组合总和();
        System.out.println(instance.combinationSum(new int[]{10, 1, 2, 7, 6, 1, 5}, 8));
    }
}
