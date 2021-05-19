package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/3/17 8:51 下午
 * https://leetcode-cn.com/problems/maximum-number-of-balloons/
 * 气球的最大数量
 *
 * 给你一个字符串 text，你需要使用 text 中的字母来拼凑尽可能多的单词 "balloon"（气球）。
 *
 * 字符串 text 中的每个字母最多只能被使用一次。请你返回最多可以拼凑出多少个单词 "balloon"。
 *
 *示例 1：
 *
 *
 *
 * 输入：text = "nlaebolko"
 * 输出：1
 * 示例 2：
 *
 *
 *
 * 输入：text = "loonbalxballpoon"
 * 输出：2
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
