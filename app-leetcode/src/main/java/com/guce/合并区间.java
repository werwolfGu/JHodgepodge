package com.guce;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2021/5/24 9:41 下午
 * https://leetcode-cn.com/problems/merge-intervals/
 */
public class 合并区间 {

    private Comparator<int[]> comparator = new Comparator<int[]>() {
        @Override
        public int compare(int[] o1, int[] o2) {
            return o1[0] - o2[0];
        }
    };
    public int[][] merge(int[][] intervals) {

        List<String> list = new ArrayList();
        Deque<int[]> stack = new ArrayDeque();
        Arrays.sort(intervals,comparator);
        for (int i = 0 ; i < intervals.length ; i++){
            int[] arr = intervals[i];
            if (stack.isEmpty()){
                stack.addLast(arr);
                continue;
            }
            int[] arrStack = stack.peekLast();
            if (arrStack[1] >= arr[0] && arrStack[1] <= arr[1]){

                arrStack[1] = arr[1];
            }else if (arrStack[1] < arr[0]) {
                stack.addLast(arr);
            }
        }

        int[][] result = new int[stack.size()][2];
        int idx = 0;
        while ( !stack.isEmpty()){
            result[idx++] = stack.pop();
        }
        return result;
    }

    public int[][] merge1(int[][] intervals) {
        Deque<int[]> stack = new ArrayDeque();

        Arrays.sort(intervals,comparator);
        for (int i = 0 ; i < intervals.length ;i++ ){
            int[] interval = intervals[i];
            if (stack.isEmpty()){
                stack.addLast(interval);
                continue;
            }
            int[] stackArr = stack.peekLast();
            if (stackArr[1] + 1 >= interval[0] && stackArr[1] + 1 <= interval[1]){
                stackArr[1] = interval[1];
            }else if (stackArr[1] + 1 < interval[0]){
                stack.addLast(interval);
            }
        }
        int[][] result = new int[stack.size()][2];
        int idx= 0;
        while (!stack.isEmpty()){
            result[idx++] = stack.pop();
        }
        return result;
    }

    public static void main(String[] args) {


        合并区间 instance = new 合并区间();
        int[][] param = new int[][]{{2,6},{1,3},{8,10},{15,18}};
        int[][] ints = instance.merge(param);
        System.out.println(ints);

        int[][] param1 = new int[][]{{1,2},{3,5},{6,7},{8,10},{12,16}};
        int[][] result = instance.merge1(param1);
        System.out.println(result);
    }
}
