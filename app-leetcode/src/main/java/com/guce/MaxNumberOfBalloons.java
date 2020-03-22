package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/3/17 8:51 下午
 * https://leetcode-cn.com/problems/maximum-number-of-balloons/
 * 气球的最大数量
 */
public class MaxNumberOfBalloons {

    public static int solution(String text){

        int[] letter = new int[26];
        for (int i = 0 ; i < text.length() ; i++){
            char ch = text.charAt(i);
            letter[ch - 97]++ ;
        }
        int min = Integer.MAX_VALUE;
        letter['l' - 97] /= 2;
        letter['o' - 97] /= 2;
        for (char ch : "balon".toCharArray()){
            if (min > letter[ch - 97]){
                min = letter[ch -97];
            }
        }
        return min;
    }

    public static void main(String[] args) {
        System.out.println(solution("loonbalxballpoon"));
        System.out.println(solution("xyz"));
    }

}
