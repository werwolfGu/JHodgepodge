package com.guce.backtrace;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2020/9/10 8:36 下午
 */
public class combinationSum2 {

    public  static List<List<Integer>> combinationSum2(int[] candidates, int target){

        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(candidates);
        Deque<Integer> queue = new ArrayDeque<>();
        backtrace(candidates,target,0,queue,result);
        return result;
    }

    private static void backtrace(int[] candidates , int target , int begin, Deque<Integer> list , List<List<Integer>> result){

        if (target == 0){
            result.add(new ArrayList<>(list));
            return ;
        }

        for (int i = begin ;i < candidates.length ; i++){
            if (target - candidates[i] < 0){
                return;
            }
            // 小剪枝  既然不能重复当前元素，那么利用排序，将相邻两个相同的元素只取前一个去组合，当前直接跳过，直接进入下一个元素进行组合
            if (i > begin && candidates[i] == candidates[i - 1]) {
                continue;
            }
            list.add(candidates[i]);
            // 因为元素不可以重复使用，这里递归传递下去的是 i + 1 而不是 i
            backtrace(candidates,target - candidates[i], i+ 1 ,list,result);
            list.removeLast();
        }
    }

    public static void main(String[] args) {
        StringBuffer s ;


        System.out.println(combinationSum2(new int[]{1,2,5},5));
    }
}
