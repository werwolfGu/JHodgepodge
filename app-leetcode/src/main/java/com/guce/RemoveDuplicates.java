package com.guce;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @Author chengen.gu
 * @DATE 2019/10/26 3:49 下午
 * https://leetcode-cn.com/problems/remove-duplicates-from-sorted-array-ii/submissions/
 */
public class RemoveDuplicates {


    public int removeDuplicate(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                if ( i != j){
                    nums[i + 1] = nums[j];
                }

                i++;
            }
        }
        return i + 1;
    }

    public static int removeDuplicates2(int nums[]) {

        if (nums == null) {
            return 0;
        }
        if (nums.length < 3) {
            return nums.length;
        }
        int idx = 2;
        for (int i = 2; i < nums.length; i++) {
            if (nums[idx - 2] != nums[i]) {
                nums[idx] = nums[i];
                idx++;
            }
        }
        return idx;
    }


    public String removeDuplicates(String s, int k) {

        StringBuilder sb = new StringBuilder();
        Deque<Integer> stack = new ArrayDeque();
        int top = -1;
        for (int i = 0; i < s.length(); i++) {
            if (sb.length() > 0) {
                if (sb.charAt(top) != s.charAt(i)) {
                    top++;
                    stack.addLast(1);
                    sb.append(s.charAt(i));
                } else {
                    int count = stack.pollLast() + 1;
                    top++;
                    stack.addLast(count);
                    sb.append(s.charAt(i));
                    if (count == k) {
                        stack.pollLast();
                        sb.delete(top - count + 1, top + 1);
                        top = top - count;

                    }
                }
            } else {
                top++;
                stack.addLast(1);
                sb.append(s.charAt(i));
            }

        }
        return sb.toString();
    }


    public static void main(String[] args) {
        RemoveDuplicates duplicates = new RemoveDuplicates();
        int[] nums = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4, 5};
        System.out.println("");
        nums = new int[]{1, 1, 1, 2, 2, 3};
        System.out.println(removeDuplicates2(nums));
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i] + " ");
        }
        System.out.println("");
        nums = new int[]{0, 0, 1, 1, 1, 1, 2, 3, 3};
        System.out.println(removeDuplicates2(nums));
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i] + " ");
        }


        System.out.println(duplicates.removeDuplicates("deeedbbcccbdaa", 3));

    }
}
