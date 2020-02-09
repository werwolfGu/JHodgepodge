package com.guce;

/**
 * Created by chengen.gu on 2018/9/18.
 * https://leetcode-cn.com/problems/integer-to-roman/description/
 * 将整形转换成罗马数
 * 思路：将罗马数与对应的整数映射好，从最大的数减去int整数
 */
public class IntToRoman {

    private static int numbers[] = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};

    private static String roman[] = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    public static String solution(int num) {


        StringBuilder result = new StringBuilder();
        for (int i = 0; i < numbers.length; i++) {
            while (num >= numbers[i]) {
                num -= numbers[i];
                result.append(roman[i]);
            }
        }

        return result.toString();

    }

    public static void main(String[] args) {
        System.out.println(solution(2999));
    }
}
