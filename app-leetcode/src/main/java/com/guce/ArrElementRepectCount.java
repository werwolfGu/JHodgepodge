package com.guce;

import java.util.Arrays;

/**
 * 求数组元素重复的次数。
 * @Author chengen.gce
 * @DATE 2021/3/5 10:48 上午
 */
public class ArrElementRepectCount {

    public static int[] solution(int arr[]){

        int i = 0 , len = arr.length;
        while( i < len){
            int tmp = arr[i];
            if (tmp <= 0){
                i++ ;
                continue;
            }
            if (arr[tmp] > 0){
                arr[i] = arr[tmp];
                arr[tmp] = 0;
            }else {

                i++ ;
            }
            arr[tmp]-- ;
        }
        return arr;
    }

    public static void main(String[] args) {
        int[] arr = solution(new int[]{1,2,3,1,4,4,4,5});
        Arrays.stream(arr).forEach( i -> System.out.print( i + " "));
    }
}
