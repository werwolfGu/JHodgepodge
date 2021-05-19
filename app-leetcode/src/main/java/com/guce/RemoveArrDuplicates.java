package com.guce;

import java.util.Arrays;

/**
 * @Author chengen.gu
 * @DATE 2020/2/16 8:39 下午
 *
 * https://leetcode-cn.com/problems/remove-duplicates-from-sorted-array/submissions/
 * 删除排序数组中的重复项
 */
public class RemoveArrDuplicates {

    public static int solution(int[] arr){

        if (arr == null || arr.length <= 0){
            return 0;
        }

        int idx = 0;
        for (int i = 1 ; i < arr.length ; i++){

            if (arr[idx] != arr[i]){
                idx++;
            }
            arr[idx] = arr[i];
        }
        return idx + 1;
    }

    public static void main(String[] args) {

        int[] arr = new int[]{1,1,2};
        RemoveArrDuplicates.solution(arr);

        Arrays.stream(arr).forEach( x -> System.out.print(x + " "));
    }
}
