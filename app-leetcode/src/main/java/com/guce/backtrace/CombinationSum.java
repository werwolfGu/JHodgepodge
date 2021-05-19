package com.guce.backtrace;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2020/4/18 2:50 下午
 * https://leetcode-cn.com/problems/combination-sum/
 * 组合总和
 */
public class CombinationSum {

    public static List<List<Integer>> solution(int[] candidates , int target){

        List<List<Integer>> result = new ArrayList<>();

        Deque<Integer> list = new ArrayDeque<>();
        backtrack(candidates,target,list,result,0);
        return result;
    }

    public static void backtrack(int[] candidates , int target
            ,Deque<Integer> list,List<List<Integer>> result,int idx){

        if (target == 0){
            result.add(new ArrayList<>(list));
        }

        for (int i = idx ; i < candidates.length ; i++ ){
            if (target < candidates[i]){
                continue;
            }
            list.add(candidates[i]);
            backtrack(candidates,target - candidates[i],list,result,i);
            list.removeLast();
        }
    }

    public static void main(String[] args) {
        System.out.println(solution(new int[]{8,7,4,3},11));
        System.out.println(solution(new int[]{6,3,2,7},7));
        System.out.println(solution(new int[]{1,2,5},5));
    }
}
