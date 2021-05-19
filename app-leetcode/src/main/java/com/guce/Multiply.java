package com.guce;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2020/6/8 10:52 下午
 * https://leetcode-cn.com/problems/multiply-strings/
 */
public class Multiply {

    public static String solution(String num1, String num2){
        if ("0".equals(num1) || "0".equals(num2)){
            return "0";
        }
        List<String> list = new ArrayList<>();
        int idx = 0;
        //算出乘积
        for (int i = num2.length()  -1 ; i >= 0 ; i--){
            String x = "";
            //下一个数会多一个0
            for (int j = 0 ; j < idx ;j++ ){
                x = x +"0";
            }
            idx++;
            int multi = 0;
            for (int j =  num1.length() -1 ; j >= 0;j-- ){
                int a = num2.charAt(i) - '0';
                int b = num1.charAt(j) - '0';
                int c = a * b + multi;
                multi = 0;
                if (c >= 10){
                    multi = c / 10;
                    c = c % 10 ;
                }
                x = c + x;
            }
            if (multi > 0){
                x = multi + x;
            }
            list.add(x);
        }
        String sum = list.get(list.size() -1);
        //算出各个数字的和
        for (int i = list.size() -2 ; i >= 0 ; i-- ){
            String str = list.get(i);
            int multi = 0 ;
            StringBuilder tmpSum = new StringBuilder();
            int len = str.length() - 1;
            for (int j = sum.length() -1 ; j >= 0 ; j--){
                int a = sum.charAt(j) - '0';

                int b = 0 ;
                if (len >= 0 ){
                    b = str.charAt(len) - '0';
                    len--;
                }
                int c = a + b + multi;
                multi = c /10 ;
                c = c % 10 ;
                tmpSum.insert(0, c);
            }
            if (multi > 0){
                tmpSum.insert(0, multi);
            }
            sum = tmpSum.toString();

        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(solution("123","456"));
    }
}
