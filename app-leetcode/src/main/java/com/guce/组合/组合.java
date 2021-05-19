package com.guce.组合;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2021/5/16 9:57 下午
 */
public class 组合 {

    public List<List<Integer>> combine(int n, int k) {

        List<List<Integer>> res = new ArrayList<>();
        Deque<Integer> stack = new ArrayDeque();
        dfs(1,n,k,stack,res);
        return res;
    }

    public void dfs(int begin , int n , int k , Deque<Integer> stack ,List<List<Integer>> result ){
        if (stack.size() == k){
            result.add(new ArrayList<>(stack));
            return ;
        }

        for (int i = begin ; i <= n ; i++ ){
            stack.addLast(i);
            dfs(i + 1 , n,k,stack,result);
            stack.pollLast();
        }
    }

    public static void main(String[] args) {

        组合 instance = new 组合();
        System.out.println(instance.combine(4,2));

    }
}
