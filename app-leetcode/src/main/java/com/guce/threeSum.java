package com.guce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by chengen.gu on 2018/9/27.
 * https://leetcode-cn.com/problems/3sum/description/
 */
public class threeSum {

    public static List<List<Integer>> solution(int[] nums) {

        List<List<Integer>> result = new ArrayList<>();


        Arrays.sort(nums);
        int len = nums.length;
        if (nums == null || len < 3 || nums[0] > 0) {
            return result;
        }
        for (int i = 0; i < len - 2; i++) {

            if (nums[i] > 0) break;
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue; // 去重
            }
            int j = i + 1;
            int k = len - 1;
            while (j < k) {
                int sum = nums[i] + nums[j] + nums[k];
                if (sum > 0) {
                    k--;
                    continue;
                }
                if (sum < 0) {
                    j++;
                    continue;
                }
                List<Integer> tmpList = Arrays.asList(nums[i], nums[j], nums[k]);
                result.add(tmpList);
                while (j < k && nums[j] == nums[j + 1]) {
                    j++; // 去重
                }
                while (j < k && nums[k] == nums[k - 1]) {
                    k--; // 去重
                }
                j++;
                k--;

            }

        }
        return result;
    }


    public static void main(String[] args) {

        int[] nums = new int[]{-1, 0, 1};
        List<List<Integer>> list = solution(nums);
        System.out.println("nums length :" + nums.length + "  list size:" + list.size() + "\n" + list);

        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        list1.add(Integer.valueOf(1));
        list1.add(new Integer(2));
        list1.add(new Integer(3));
        list2.add(new Integer(1));
        list2.add(new Integer(2));
        list2.add(new Integer(3));
        Set<List<Integer>> set = new HashSet<>();
        set.add(list1);
        set.add(list2);
        System.out.println(list1.hashCode() == list2.hashCode());
        System.out.println(list1 == list2);
        System.out.println(set);
    }

}
