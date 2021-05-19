package com.guce;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author chengen.gce
 * @DATE 2020/6/6 11:30 下午
 * https://leetcode-cn.com/problems/longest-consecutive-sequence/
 * <p>
 * 给定一个未排序的整数数组，找出最长连续序列的长度。
 * <p>
 * 要求算法的时间复杂度为 O(n)。
 * <p>
 * 示例:
 * <p>
 * 输入: [100, 4, 200, 1, 3, 2]
 * 输出: 4
 * 解释: 最长连续序列是 [1, 2, 3, 4]。它的长度为 4。
 */
public class LongestConsecutive_0606 {

    public static int solution(int[] nums) {

        Set<Integer> set = new HashSet<>();

        for (int value : nums) {
            set.add(value);
        }

        int longest = 0;
        for (int num : nums) {

            if (!set.contains(num - 1)) {
                int currNum = num + 1;
                int currLongest = 1;
                while (set.contains(currNum)) {
                    currLongest++;
                    currNum++;
                }
                longest = Math.max(currLongest, longest);
            }

        }

        return longest;
    }

    public static int longestConsecutive(int[] nums) {

        Set<Integer> set = new HashSet<>();
        for (int i = 0 ; i < nums.length ; i++ ){
            set.add(nums[i]);

        }
        int longest = 0;
        for (int i = 0 ; i < nums.length ; i++ ){

            int x = nums[i];
            int currLongest = 0;
            if (set.contains(x)){
                while (set.contains(x++)){
                    currLongest++ ;
                }
                longest = Math.max(longest,currLongest);
            }
        }
        return longest;
    }

    public static void main(String[] args) {

        System.out.println(solution(new int[]{0,3,7,2,5,8,4,6,0,1}));
        System.out.println(longestConsecutive(new int[]{0,3,7,2,5,8,4,6,0,1}));
    }
}
