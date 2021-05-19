package com.guce;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2020/4/25 5:23 下午
 * https://leetcode-cn.com/problems/permutations/submissions/
 * <p>
 * 全排列
 */
public class Permute {

    public static List<List<Integer>> solution(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        LinkedList list = new LinkedList();
        backtrace(nums, res, list);
        return res;
    }

    public static void backtrace(int[] nums, List<List<Integer>> res, LinkedList<Integer> list) {
        if (nums.length == list.size()) {
            res.add(new ArrayList<>(list));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (list.contains(nums[i])) {
                continue;
            }
            list.add(nums[i]);
            backtrace(nums, res, list);
            list.removeLast();
        }
    }

    public static List<List<Integer>> solution2(int[] nums) {

        List<List<Integer>> res = new ArrayList<>();
        boolean used[] = new boolean[nums.length];
        Deque<Integer> path = new ArrayDeque<>();
        backtrace(nums, used, path, res);
        return res;
    }

    public static void backtrace(int nums[], boolean used[], Deque<Integer> path, List<List<Integer>> res) {
        if (nums.length == path.size()) {
            res.add(new ArrayList<>(path));
        }

        for (int i = 0; i < nums.length; i++) {
            if (used[i]) {
                continue;
            }
            used[i] = true;
            path.addLast(nums[i]);
            backtrace(nums, used, path, res);
            used[i] = false;
            path.removeLast();

        }
    }

    public static void main(String[] args) {
        System.out.println(solution(new int[]{1, 2, 3}));
        System.out.println(solution2(new int[]{1, 2, 3}));
    }
}
