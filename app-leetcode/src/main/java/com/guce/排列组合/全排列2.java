package com.guce.排列组合;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2021/5/16 9:49 下午
 */
public class 全排列2 {

    public List<List<Integer>> permuteUnique(int[] nums) {

        Arrays.sort(nums);

        List<List<Integer>> result  = new ArrayList();
        Deque<Integer> stack = new ArrayDeque<>();
        boolean[] used = new boolean[nums.length];
        dfs(nums,used,stack,result);
        return result;

    }

    public void dfs (int[] nums , boolean[] used , Deque<Integer> stack ,List<List<Integer>> result ){

        if (stack.size() == nums.length){
            result.add(new ArrayList<>(stack));
            return ;
        }

        for (int i = 0 ; i < nums.length ; i++ ){
            if (used[i]){
                continue;
            }
            if (i > 0 && used[ i -1] && nums[i -1] == nums[i]){
                continue;
            }
            used[i] = true;
            stack.addLast(nums[i]);
            dfs(nums,used,stack,result);
            used[i] = false;
            stack.pollLast();
        }

    }

    public static void main(String[] args) {

        全排列2 instance = new 全排列2();
        System.out.println(instance.permuteUnique(new int[]{1,1,2}));
    }
}
