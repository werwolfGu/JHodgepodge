package com.guce.backtrace;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2020/6/22 10:15 下午
 * https://leetcode-cn.com/problems/permutation-sequence/
 * 第k个排列
 */
public class GetPermutation {

    public static String solution(int n , int k){

        int[] nums = new int[n];
        for (int i = 0 ; i < n ; i++){
            nums[i] = i + 1;
        }
        Deque<Integer> path = new ArrayDeque<>();

        List<List<Integer>> res = new ArrayList<>();
        boolean[] used = new boolean[n];
        backtrace(nums,path,used,res,k);
        if (res.size() < k){
            return "";
        }
        List<Integer> result = res.get(k - 1);
        String str = "";
        for (int i = 0 ; i < result.size() ; i++ ){
            str = str + result.get(i);
        }
        return str;
    }

    public static void backtrace(int[] nums, Deque<Integer> path, boolean[] used , List<List<Integer>> res,int k){
        if (nums.length == path.size()){
            res.add(new ArrayList<>(path));
            return ;
        }
        if (res.size() == k){
            return ;
        }


        for (int i = 0 ; i < nums.length ;i++ ){
            if (used[i]){
                continue;
            }
            path.addLast(nums[i]);
            used[i] = true;
            backtrace(nums,path,used,res,k);
            path.removeLast();
            used[i] = false;

        }
    }

    public static void main(String[] args) {

        String list = solution(3,3);
        System.out.println(list);
    }
}
