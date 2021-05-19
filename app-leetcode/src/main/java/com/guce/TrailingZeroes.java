package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2021/2/6 9:10 下午
 * https://leetcode-cn.com/problems/factorial-trailing-zeroes/
 *
 * 给定一个整数 n，返回 n! 结果尾数中零的数量。
 *
 * 示例 1:
 *
 * 输入: 3
 * 输出: 0
 * 解释: 3! = 6, 尾数中没有零。
 * 示例 2:
 *
 * 输入: 5
 * 输出: 1
 * 解释: 5! = 120, 尾数中有 1 个零.
 *
 */
public class TrailingZeroes {

    public int trailingZeroes(int n) {
        int res = 0 ;
        for (int i = 5 ; i <= n ; i = i + 5 ){
            res += modFive(i);

        }
        return res;
    }

    public int modFive(int x ){
        int m = 0 ;
        while (true){
            if (x % 5 == 0){
                m++ ;
                x /= 5;
                continue;
            }
            break;
        }
        return m;
    }

    public static void main(String[] args) {
        TrailingZeroes obj = new TrailingZeroes();
        System.out.println(obj.trailingZeroes(30));
    }
}
