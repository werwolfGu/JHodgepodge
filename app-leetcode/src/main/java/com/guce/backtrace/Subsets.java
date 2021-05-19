package com.guce.backtrace;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2021/3/23 1:20 下午
 * https://leetcode-cn.com/problems/subsets/
 * 给你一个整数数组 nums ，数组中的元素 互不相同 。返回该数组所有可能的子集（幂集）。
 *
 * 解集 不能 包含重复的子集。你可以按 任意顺序 返回解集。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：nums = [1,2,3]
 * 输出：[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
 * 示例 2：
 *
 * 输入：nums = [0]
 * 输出：[[],[0]]
 *
 */
public class Subsets {

    public List<List<Integer>> subsets(int[] nums) {

        Deque<Integer> queue = new ArrayDeque<>();
        List<List<Integer>> res = new ArrayList<>();
        backtrack(nums,0,queue,res);
        return res;

    }

    public void backtrack (int[] nums , int idx ,  Deque<Integer> list ,List<List<Integer>> result ){
        result.add(new ArrayList<>(list));

        for (int i = idx ; i < nums.length ;i++ ){

            list.add(nums[i]);
            backtrack(nums,i + 1 ,list,result);
            list.removeLast();
        }
    }

    public static void main(String[] args) {

        Subsets s = new Subsets();
        System.out.println(s.subsets(new int[]{1,2,3}));
    }
}
