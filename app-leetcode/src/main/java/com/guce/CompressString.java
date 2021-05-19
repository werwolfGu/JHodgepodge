package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/3/16 8:21 下午
 * https://leetcode-cn.com/problems/compress-string-lcci/
 * 字符串压缩
 */
public class CompressString {

    public static String solution(String s){

        String src = s;

        StringBuffer sb = new StringBuffer();

        int idx = 0 ;
        sb.append(s.charAt(idx));
        int currIdx = 1;
        for (int i = 1 ; i < s.length() ; i++ ){

            if (s.charAt(idx) == s.charAt(i)){
                currIdx++ ;
            }else {
                idx = i;

                sb.append(currIdx);
                currIdx = 1 ;
                sb.append(s.charAt(idx));

            }
        }
        sb.append(currIdx);
        if (s.length() <= sb.length()){
            return src;
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(solution("aabcccccaaa"));
        System.out.println(solution("abcd"));
    }
}
