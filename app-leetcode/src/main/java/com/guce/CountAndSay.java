package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/5/24 5:25 下午
 */
public class CountAndSay {

    public static String solution(int n){
        if (n == 1){
            return "1";
        }

        String str = solution(n -1);

        int len = str.length();
        int first = 0 ;
        int idx = 1;
        StringBuilder sb = new StringBuilder();
        while (first < len){
            int s = first + 1;
            if (s > len){
                break;
            }
            char ch = str.charAt(first);
            char ch2 = 'a';
            if (s < len){
                ch2 = str.charAt(s);
            }
            if (ch == ch2){
                idx++;
            }else {
                sb.append(idx).append(ch);
                idx = 1;
            }
            first++ ;
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(solution(3));
    }
}
