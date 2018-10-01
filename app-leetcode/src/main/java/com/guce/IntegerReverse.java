package com.guce;

/**
 * Created by chengen.gu on 2018/9/18.
 * 给定一个 32 位有符号整数，将整数中的数字进行反转。
 *
 *
    示例 1:
    输入: 123
    输出: 321

    示例 2:
    输入: -123
    输出: -321

    示例 3:
    输入: 120
    输出: 21

 */
public class IntegerReverse {

    public static int solution(int x){
        int result = 0;

        while (x != 0){

            int mod = x % 10;
            x /= 10;
            if(result >  Integer.MAX_VALUE /10 || ( result == Integer.MAX_VALUE / 10 && mod == 7 )){
                return 0;
            }

            if(result <  Integer.MIN_VALUE /10 || ( result == Integer.MIN_VALUE / 10 && mod == 8 )){
                return 0;
            }

            result = result * 10 + mod;
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println(solution(1534236469));
        System.out.println(-8 % 10);
    }
}
