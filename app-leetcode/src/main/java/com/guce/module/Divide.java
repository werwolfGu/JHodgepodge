package com.guce.module;

/**
 * @Author chengen.gu
 * @DATE 2019/11/5 9:57 下午
 */
public class Divide {

    public static int solution(int dividend,int divisor){
        boolean sign = (dividend > 0) ^ (divisor > 0);
        int result = 0;
        if(dividend>0) {
            dividend = -dividend;
        }
        if(divisor>0) {
            divisor = -divisor;
        }
        while(dividend <= divisor) {
            int temp_result = -1;
            int temp_divisor = divisor;
            while(dividend <= (temp_divisor << 1)) {
                if(temp_divisor <= (Integer.MIN_VALUE >> 1)){
                    break;
                }
                temp_result = temp_result << 1;
                temp_divisor = temp_divisor << 1;
            }
            dividend = dividend - temp_divisor;
            result += temp_result;
        }
        if(!sign) {
            if(result <= Integer.MIN_VALUE) {
                return Integer.MAX_VALUE;
            }
            result = - result;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(Divide.solution(5,3));
    }
}
