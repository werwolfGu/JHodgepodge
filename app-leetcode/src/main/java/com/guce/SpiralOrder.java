package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/6/5 9:35 下午
 * https://leetcode-cn.com/problems/shun-shi-zhen-da-yin-ju-zhen-lcof/
 * 顺时针打印矩阵
 */
public class SpiralOrder {

    public static int[] solution(int[][] matrix){
        if (matrix == null){
            return null;
        }
        int[] result = new int[matrix.length * matrix[0].length];

        int l = 0, r = matrix[0].length - 1, t = 0, b = matrix.length - 1, x = 0;

        while (true) {

            //左到右
            for (int i = l ; i <= r ; i++) {
                result[x++] = matrix[t][i];
            }
            if (++t > b){
                break;
            }

            //上到下
            for (int i = t ; i <= b ; i++ ){
                result[x++] = matrix[i][r];
            }

            if ( l > --r){
                break;
            }
            //right to left
            for (int i = r ;i >= l ; i--){
                result[x++] = matrix[b][i];
            }

            if(t > --b){
                break;
            }
            // bottom to top.
            for(int i = b; i >= t; i--) {
                result[x++] = matrix[i][l];
            }
            if(++l > r) {
                break;
            }


        }
        return result;
    }
}
