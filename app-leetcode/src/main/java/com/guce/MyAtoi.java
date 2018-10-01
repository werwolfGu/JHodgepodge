package com.guce;

/**
 * Created by chengen.gu on 2018/9/28.
 * https://leetcode-cn.com/problems/string-to-integer-atoi/
 * 字符串转整数
 */
public class MyAtoi {

    public static int solution(String str){
        int result = 0 ;
        if(str.length() <= 0){
            return 0;
        }
        boolean flag = true;
        str = str.trim();

        for(int i = 0 ; i < str.length() ; i++ ){

            char ch = str.charAt(i);


            if(ch == '+' && i == 0) continue;
            if( ch == '-' && i == 0){
                flag = false;
                continue;
            }

            if(ch < 48 || ch > 57){
                break;
            }

            if(result > Integer.MAX_VALUE/10 || (result == Integer.MAX_VALUE/10 && (ch - 48) >= 7)) return Integer.MAX_VALUE;
            if(result < Integer.MIN_VALUE/10 || (result == Integer.MIN_VALUE/10 && (ch - 48) >= 8)) return Integer.MIN_VALUE;


            if(!flag){
                result = result * 10 -  (ch - 48);
            }else{
                result = result * 10 +  (ch - 48);
            }
        }

        return result;
    }
    public static void main(String[] args) {
        System.out.println(solution("-1-")  + "  " + (int)'+');
    }
}
