package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/4/6 7:51 下午
 * https://leetcode-cn.com/problems/reverse-integer/
 * 整数反转
 *  123 输出  321
 *  -123 输出 -321
 */
public class Reverse {

    public static int solution (int x ){
        int res = 0 ;
        while (x != 0){
            int mod = x % 10 ;

            if (res > Integer.MAX_VALUE / 10 || (res == Integer.MAX_VALUE / 10 && mod > 7))
                return 0;
            if (res < Integer.MIN_VALUE / 10 || (res == Integer.MIN_VALUE / 10 && mod < -8))
                return 0;
            res = res * 10 + mod;
            x /= 10;
        }

        return res;
    }

    public static void main(String[] args) {
        System.out.println(solution(1534236469));

        System.out.println(Integer.MIN_VALUE );
        System.out.println(Integer.MAX_VALUE );

    }
}
