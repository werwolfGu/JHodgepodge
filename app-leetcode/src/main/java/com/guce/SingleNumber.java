package com.guce;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author chengen.gce
 * @DATE 2020/5/26 11:14 下午
 * https://leetcode-cn.com/problems/single-number/
 * 136. 只出现一次的数字
 * 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
 */
public class SingleNumber {

    /**
     * 每个数字出现2次；其中有1个数字出现1次
     *
     * @param nums
     * @return
     */
    public static int solution(int[] nums) {
        int single = 0;
        for (int i = 0; i < nums.length; i++) {
            single ^= nums[i];
        }
        return single;
    }

    /**
     * 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现三次。找出那个只出现了一次的元素。
     *
     * @param nums
     * @return
     */
    public static int solution2(int[] nums) {
        Map<Integer,Integer> map = new HashMap<>();
        for (Integer num :nums){
            map.put(num,map.getOrDefault(num,0) + 1);
        }
        for (Map.Entry<Integer,Integer> entry : map.entrySet()){
            Integer value = entry.getValue();
            if (1 == value){
                return entry.getKey();
            }
        }
        return -1;
    }


    public static void main(String[] args) {
        System.out.println(solution(new int[]{2, 3, 3, 2, 4}));
    }
}
