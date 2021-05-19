package com.guce.backtrace;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2020/9/11 7:35 下午
 * https://leetcode-cn.com/problems/combination-sum-iii/
 *
 * 找出所有相加之和为 n 的 k 个数的组合。组合中只允许含有 1 - 9 的正整数，并且每种组合中不存在重复的数字。
 */
public class CombinationSum3 {

    public List<List<Integer>> combinationSum3(int k, int n){

        List<List<Integer>> results = new ArrayList<>();
        Deque<Integer> deque = new ArrayDeque();
        boolean[] used = new boolean[10];
        backtrace(k,n,1,deque,used,results);
        return results;

    }
    public void backtrace(int k,int n,int begin,Deque<Integer> deque , boolean[] used,List<List<Integer>> results){

        if (deque.size() == k){
            int sum = 0 ;
            for (Integer i : deque){
                sum += i;
            }
            if (sum == n){
                results.add(new ArrayList<>(deque));
            }
            return ;
        }

        for (int i = begin ; i <= 9 ; i++ ){
            if (used[i]){
                continue;
            }

            deque.add(i);
            used[i] = true;
            backtrace(k,n ,i + 1 ,deque,used,results);
            used[i] = false;
            deque.removeLast();
        }
    }

    public static void main(String[] args) {

        CombinationSum3 combinationSum3 = new CombinationSum3();
        List<List<Integer>> list = combinationSum3.combinationSum3(3, 7);
        System.out.println(list);
    }
}
