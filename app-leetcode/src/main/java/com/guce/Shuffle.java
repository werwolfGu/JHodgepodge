package com.guce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author chengen.gce
 * @DATE 2020/11/22 5:25 下午
 * https://leetcode-cn.com/problems/shuffle-the-array/
 */
public class Shuffle {

    public static int[] shuffle(int[] nums, int n) {

        int result[] = new int[nums.length];

        int idx = 0 ;
        while(idx < n){
            int i = idx * 2;
            result[i++] = nums[idx];
            result[i] = nums[idx+ n];
            idx++ ;
        }
        return result ;
    }

    public static void main(String[] args) {
        int nums[] =new  int[]{2,5,1,3,4,7} ;

        int[] result = shuffle(nums,3);

        for (int i : result) {
            System.out.print(i + " ");
        }

        String str = "sdada";
        char[] ch = str.toCharArray();
        Arrays.sort(ch);
        str = new String(ch);

        Map<String,List<String>> map = new HashMap<>();
        String key = "";
        List<String> list = map.getOrDefault(key,map.getOrDefault(key,new ArrayList<String>()));

    }
}
