package com.guce.backtrace;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2020/9/10 8:59 下午
 */
public class PermuteUnique {

    public static List<List<Integer>> permuteUnique(int[] nums){

        List<List<Integer>> result = new ArrayList<>();

        Deque<Integer> deque = new ArrayDeque();
        boolean used[] = new boolean[nums.length];

        return result;
    }

    public static void backtrace(int[] nums , Deque<Integer> deque , List<List<Integer>> result ,boolean[] used){

        if (deque.size() == nums.length){
            result.add(new ArrayList<>(deque));
            return ;
        }
        for (int i = 0 ; i < nums.length ; i++ ){
            if (used[i]){
                continue;
            }
            if (i > 0 && nums[i -1] == nums[i] && !used[i - 1 ]){
                continue;
            }
            deque.add(nums[i]);
            used[i] = true;
            backtrace(nums,deque,result,used);
            used[i] = false;
            deque.removeLast();
        }
    }
}
