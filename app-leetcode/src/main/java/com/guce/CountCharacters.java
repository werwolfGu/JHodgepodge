package com.guce;

import java.util.concurrent.CompletableFuture;

/**
 * @Author chengen.gce
 * @DATE 2020/3/17 9:00 下午
 * https://leetcode-cn.com/problems/find-words-that-can-be-formed-by-characters/
 * 拼写单词
 *
 * 给你一份『词汇表』（字符串数组） words 和一张『字母表』（字符串） chars。
 *
 * 假如你可以用 chars 中的『字母』（字符）拼写出 words 中的某个『单词』（字符串），那么我们就认为你掌握了这个单词。
 *
 * 注意：每次拼写（指拼写词汇表中的一个单词）时，chars 中的每个字母都只能用一次。
 *
 * 返回词汇表 words 中你掌握的所有单词的 长度之和。
 *
 * 输入：words = ["cat","bt","hat","tree"], chars = "atach"
 * 输出：6
 * 解释：
 * 可以形成字符串 "cat" 和 "hat"，所以答案是 3 + 3 = 6。
 *
 *
 */
public class CountCharacters {

    public static int solution(String[] words,String chars){
        int[] letter = new int[26];
        for (int i = 0 ; i < chars.length() ; i++ ){
            letter[chars.charAt(i) - 97]++;
        }
        int len = 0;
        for (int i = 0 ; i < words.length ; i++ ){
            int[] tmp = letter.clone();
            boolean flag = true;
            int len1 = 0 ;
            for (char ch : words[i].toCharArray()){
                if (tmp[ ch - 97] == 0){
                    flag = false;
                    break;
                }
                tmp[ch - 97]--;
                len1++ ;
            }
            if (flag){
                len += len1;
            }
        }
        return len;
    }

    public static void main(String[] args) {
        String[] words = {"cat","bt","hat","tree"};
        String chars = "atach";
        System.out.println(solution(words,chars));

        words = new String[]{"hello","world","leetcode"};
        chars = "welldonehoneyr";
        System.out.println(solution(words,chars));
        CompletableFuture.runAsync(() -> {
        });

    }
}
