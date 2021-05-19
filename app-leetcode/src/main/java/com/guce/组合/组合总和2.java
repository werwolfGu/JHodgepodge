package com.guce.组合;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2021/5/16 10:11 下午
 */
public class 组合总和2 {
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {

        Arrays.sort(candidates);
        Deque<Integer> stack = new ArrayDeque<>();
        List<List<Integer>> result = new ArrayList<>();
        dfs(candidates,0 ,0 ,target,stack,result);
        return result;

    }

    public void dfs (int[] nums , int begin  ,int sum, int target , Deque<Integer> stack ,List<List<Integer>> result){
        if (sum == target){
            result.add(new ArrayList<>(stack));
            return ;
        }

        for (int i = begin ; i < nums.length ; i++ ){
            if (sum + nums[i] > target){
                break;
            }
            //////剪枝
            if ( i > begin && nums[i -1] == nums[i]){
                continue;
            }
            stack.addLast(nums[i]);
            dfs(nums,i + 1, sum + nums[i] ,target, stack,result);
            stack.pollLast();
        }
    }

    public static void main(String[] args) {
        组合总和2 instance = new 组合总和2();
        System.out.println(instance.combinationSum2(new int[]{10,1,2,7,6,1,5} ,8));
    }
}
