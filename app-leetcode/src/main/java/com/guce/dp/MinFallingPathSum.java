package com.guce.dp;

/**
 * @Author chengen.gce
 * @DATE 2021/7/31 11:27 下午
 * https://leetcode-cn.com/problems/minimum-falling-path-sum/
 */
public class MinFallingPathSum {

    public static int minFallingPathSum(int[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;
        int[][] dp = new int[row][col];
        for (int j = 0 ; j < col ; j++ ){
            dp[0][j] = matrix[0][j];
        }
        for (int i = 1 ; i < row ; i++){
            for (int j = 0 ; j < col ;j++ ){
                int mid = dp[i -1][j];
                if (j > 0){
                    mid = Math.min(mid,dp[i -1][j -1]);
                }
                if (j < col - 1){
                    mid = Math.min(mid,dp[i -1][j +1]);
                }

                dp[i][j] = mid + matrix[i][j];
            }
        }
        int min = dp[row -1][0];
        for (int i = 1 ; i < col ; i++ ){
            min = Math.min(min,dp[row -1][i]);
        }
        return min;
    }

    public static void main(String[] args) {
        int[][] a = {{2,1,3},{6,5,4},{7,8,9}};

        minFallingPathSum(a);

    }
}
