package com.guce.排列组合;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2021/5/16 9:40 下午
 * https://leetcode-cn.com/problems/permutations/
 */
public class 全排列 {

    public List<List<Integer>> permute(int[] nums) {

        List<List<Integer>> result = new ArrayList<>();
        int len = nums.length;
        boolean[] used = new boolean[len];
        Deque<Integer> stack = new ArrayDeque<>();
        dfs(nums,len,used,stack,result);
        return result;
    }

    public void dfs(int[] nums , int len  ,boolean[] used, Deque<Integer> stack ,List<List<Integer>> result){
        if (stack.size() == len) {
            result.add(new ArrayList<>(stack));
            return ;
        }

        for (int i = 0 ; i < len ; i++ ){
            if (used[i]){
                continue;
            }
            stack.addLast(nums[i]);
            used[i] = true;
            dfs(nums,len ,used,stack,result);
            stack.pollLast();
            used[i] = false;
        }

    }

    public static void main(String[] args) {

        全排列 instance = new 全排列();
        System.out.println(instance.permute(new int[]{1,2,3}));
    }
}
