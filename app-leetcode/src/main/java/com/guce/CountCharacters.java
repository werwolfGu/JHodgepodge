package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/3/17 9:00 下午
 * https://leetcode-cn.com/problems/find-words-that-can-be-formed-by-characters/
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
               tmp[ch -97]--;
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

    }
}
