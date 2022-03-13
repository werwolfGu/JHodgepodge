package com.guce.组合;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2021/5/25 11:18 下午
 */
public class 子集 {

    public List<List<Integer>> subsets(int[] nums) {

        List<List<Integer>> result = new ArrayList<>();

        Deque<Integer> stack = new ArrayDeque<>();
        dfs(stack,0,nums,result);

        return result;
    }

    public void dfs(Deque<Integer>stack ,int begin,int[] nums, List<List<Integer>> result){

        result.add(new ArrayList<>(stack));

        for (int i = begin ; i < nums.length ;i++ ){

            stack.addLast(nums[i]);
            dfs(stack,i + 1 , nums,result);
            stack.pollLast();
        }

    }

    public static void main(String[] args) {
        子集 instance = new 子集();
        System.out.println(instance.subsets(new int[]{1,2,3}));
    }
}
