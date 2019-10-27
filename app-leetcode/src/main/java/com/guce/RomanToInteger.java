package com.guce;

/**
 * Created by chengen.gu on 2018/9/18.
 * https://leetcode-cn.com/problems/roman-to-integer/description/
 * 罗马数转Integer
 */
public class RomanToInteger {

    public static int solution(String s) {

        String roman[] = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        int numbers[] = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        int result = 0;
        for (int i = 0; i < roman.length; i++) {
            while (s.startsWith(roman[i])) {
                result += numbers[i];
                s = s.substring(roman[i].length());
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(solution("MCMXCIV"));
    }
}
