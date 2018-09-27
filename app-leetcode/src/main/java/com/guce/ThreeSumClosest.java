package com.guce;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by chengen.gu on 2018/9/27.
 * https://leetcode-cn.com/problems/3sum-closest/
 * 最接近的3数之和
 */
public class ThreeSumClosest {

    public static int solution(int[] nums,int target){

        int minClosest = Integer.MAX_VALUE;

        for(int i = 0 ; i < nums.length - 1;i++){

            int target1 = target - nums[i];
            for(int j = i + 1; j < nums.length ; j++ ){

            }
        }
        return 1;
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        List<Integer> list1 = new ArrayList<>();

        list.add(1);
        list.add(2);
        list1.add(2);
        list1.add(1);
        System.out.println(list.equals(list1) );

        Vector<Integer> vector = new Vector<>();
        Vector<Integer> vector1 = new Vector<>();
        vector.add(1);
        vector.add(2);
        vector1.add(1);
        vector1.add(2);
        System.out.println(vector == vector1);
    }
}
