package com.guce;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author chengen.gce
 * @DATE 2020/11/27 11:34 下午
 * https://leetcode-cn.com/problems/advantage-shuffle/
 *
 * 给定两个大小相等的数组 A 和 B，A 相对于 B 的优势可以用满足 A[i] > B[i] 的索引 i 的数目来描述。
 *
 * 返回 A 的任意排列，使其相对于 B 的优势最大化。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：A = [2,7,11,15], B = [1,10,4,11]
 * 输出：[2,11,7,15]
 * 示例 2：
 *
 * 输入：A = [12,24,8,32], B = [13,25,32,11]
 * 输出：[24,32,8,12]
 *  
 */
public class AdvantageCount {

    public int[] advantageCount(int A[], int B[]){
        Arrays.sort(A);
        int[] sortB = B.clone();
        Arrays.sort(sortB);

        Map<Integer,Deque<Integer>> assigned = new HashMap<>();

        for (int value : B) {
            assigned.put(value, new ArrayDeque<>());
        }
        ArrayDeque<Integer> reminded = new ArrayDeque();

        int idx = 0;
        for (Integer a : A){

            if (a > sortB[idx]){
                assigned.get(sortB[idx++]).add(a);
            }else {
                reminded.add(a);
            }
        }
        int[] ans = new int[B.length];

        for (int i = 0 ; i < B.length ; i++ ){
            if (assigned.get(B[i]).size() > 0){
                ans[i] = assigned.get(B[i]).pop();
            }else {
                ans[i] = reminded.pop();
            }
        }
        return ans;
    }
}
