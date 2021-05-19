package com.guce.dp;

/**
 * @Author chengen.gce
 * @DATE 2020/11/23 9:08 下午
 *
 * https://leetcode-cn.com/problems/minimum-path-sum/
 *
 * 给定一个包含非负整数的 m x n 网格 grid ，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。
 *
 * 说明：每次只能向下或者向右移动一步。
 *
 * 输入：grid = [[1,3,1],[1,5,1],[4,2,1]]
 * 输出：7
 * 解释：因为路径 1→3→1→1→1 的总和最小。
 */
public class MinPathSum {

    public static int minPathSum(int[][] grid) {

        if (grid.length <=0 || grid[0].length <= 0){
            return 0;
        }
        int m = grid.length,n = grid[0].length;
        int dp[][] = new int[m][n];
        dp[0][0] = grid[0][0];
        for (int i = 1 ; i < m ; i++){
            dp[i][0] = grid[i][0] + dp[i - 1][0];
        }
        for (int i = 1 ; i < n ; i++){
            dp[0][i] = grid[0][i] + dp[0][i -1];
        }

        for (int i = 1 ; i < m ; i++ ){
            for (int j = 1 ; j < n ;j++ ){
                dp[i][j] = grid[i][j] + Math.min(dp[i - 1][j],dp[i][j -1 ]);
            }
        }
        return dp[m -1][n - 1];

    }

    public static void main(String[] args) {

        int grid[][] = new int[][]{{1,3,1},{1,5,1},{4,2,1}};
        System.out.println(minPathSum(grid));
    }
}
