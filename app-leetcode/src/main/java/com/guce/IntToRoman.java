package com.guce;

/**
 * Created by chengen.gu on 2018/9/18.
 * https://leetcode-cn.com/problems/integer-to-roman/description/
 * 将整形转换成罗马数
 */
public class IntToRoman {

    public static String solution(int num){

        int numbers[] = new int[]{1000,900,500,400,100,90,50,40,10,9,5,4,1};

        String roman[] = new String[]{"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};

        String result = "";
        for(int i = 0 ; i < numbers.length ; i++ ){
            while (num >= numbers[i]){
                num -= numbers[i];
                result += roman[i];
            }
        }

        return result;

    }

    public static void main(String[] args) {
        System.out.println(solution(2999));
    }
}
