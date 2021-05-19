package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/3/20 10:23 下午
 * https://leetcode-cn.com/problems/integer-replacement/
 *
 * 给定一个正整数 n ，你可以做如下操作：
 *
 * 如果 n 是偶数，则用 n / 2替换 n 。
 * 如果 n 是奇数，则可以用 n + 1或n - 1替换 n 。
 * n 变为 1 所需的最小替换次数是多少？
 *
 */
public class IntegerReplacement {

    public static int solution(int n){
        int x = 0 ;

        while (n > 1){
            if ( (n & 1) == 0){
                n = n >>> 1;
                x++ ;
            }else {
                if ( (n & 2) == 0){
                    n -= 1;
                    x++;
                }else {
                    if (n == 3){
                        x += 2 ;
                        break;
                    }else {
                        n += 1;
                    }
                    x++;

                }

            }


        }
        return x;
    }

    public static int solution1(int n) {
        if (n == Integer.MAX_VALUE) {
            return 32;
        }
        if (n <= 3) {
            return n - 1;
        }
        if ((n & 2) == 0) {
            return solution1(n >>> 1) + 1;
        }
        return Math.min(solution1(n - 1), solution1(n + 1)) + 1;
    }


    public static void main(String[] args) {
        System.out.println(solution(1234));
    }

}
