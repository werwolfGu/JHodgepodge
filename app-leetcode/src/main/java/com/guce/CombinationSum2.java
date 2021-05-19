package com.guce;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2020/5/20 9:35 下午
 */
public class CombinationSum2 {
    public static List<List<Integer>> combinationSum2(int[] candidates, int target) {

        List<List<Integer>>  result = new ArrayList();
        Arrays.sort(candidates);
        backtrace(candidates,target,0,result,new ArrayDeque());
        return result;
    }

    public static void backtrace(int[] candidates , int target ,int begin,List<List<Integer>> result , Deque<Integer> list ){
        if (target == 0){
            result.add(new ArrayList(list));
            return ;
        }
        for (int i = begin ; i < candidates.length ;i++){
            if (target - candidates[i] < 0){
                break;
            }

            // 小剪枝  既然不能重复当前元素，那么利用排序，将相邻两个相同的元素只取前一个去组合，当前直接跳过，直接进入下一个元素进行组合
            if (i > begin && candidates[i] == candidates[i - 1]) {
                continue;
            }

            list.add(candidates[i]);
            backtrace(candidates ,target - candidates[i] , i + 1,result,list);
            list.removeLast();
        }
    }

    public static void main(String[] args) {

        System.out.println(combinationSum2(new int[]{10,1,2,7,6,1,5},8));
    }
}
