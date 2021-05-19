package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2021/3/28 11:50 上午
 */
public class RepeatedSubstringPattern {

    public boolean repeatedSubstringPattern(String s) {
        if (s == null){
            return true;
        }
        int len = s.length();

        for (int i = 1 ; i < len ; i++ ){
            if (len % i != 0 ){
                continue;
            }
            int count = len / i;
            String sub = s.substring(0,i);
            int idx = 1;
            boolean flag = true;
            while (idx < count ){
                if (!sub.equals(s.substring(idx * i,(idx + 1) * i))){
                    flag = false;
                    break;
                }
                idx++ ;
            }
            if (flag){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        RepeatedSubstringPattern pattern = new RepeatedSubstringPattern();
        System.out.println(pattern.repeatedSubstringPattern("babbabbabbabbab"));
    }
}
