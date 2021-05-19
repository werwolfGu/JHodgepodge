package com.guce.dp;

/**
 * @Author chengen.gce
 * @DATE 2020/9/12 10:06 上午
 *
 * https://leetcode-cn.com/problems/unique-paths-ii/
 *
 * 一个机器人位于一个 m x n 网格的左上角 （起始点在下图中标记为“Start” ）。
 *
 * 机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角（在下图中标记为“Finish”）。
 *
 * 现在考虑网格中有障碍物。那么从左上角到右下角将会有多少条不同的路径？
 *
 */
public class UniquePathsWithObstacles {

    public static int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int[][] dp = new int[obstacleGrid.length][obstacleGrid[0].length];

        int m = obstacleGrid.length, n = obstacleGrid[0].length;
        for (int i = 0 ; i < m && obstacleGrid[i][0] == 0 ; i++ ){
            dp[i][0] = 1;
        }

        for (int i = 0 ; i < n && obstacleGrid[0][i] == 0 ; i++ ){
            dp[0][i] = 1;
        }

        for (int i = 1 ; i < obstacleGrid.length ; i++ ){

            for (int j = 1 ; j < obstacleGrid[0].length  ; j++){

                if (obstacleGrid[i][j] == 0){

                    dp[i][j] = dp[i -1][j] + dp[i][j -1];

                }
            }
        }
        return dp[obstacleGrid.length -1][obstacleGrid[0].length - 1];
    }

    public static int uniquePathsWithObstacles2(int[][] obstacleGrid) {
        if (obstacleGrid == null || obstacleGrid.length == 0) {
            return 0;
        }

        // 定义 dp 数组并初始化第 1 行和第 1 列。
        int m = obstacleGrid.length, n = obstacleGrid[0].length;
        int[][] dp = new int[m][n];
        for (int i = 0; i < m && obstacleGrid[i][0] == 0; i++) {
            if (obstacleGrid[i][0] == 1){
                break;
            }
            dp[i][0] = 1;
        }
        for (int j = 0; j < n && obstacleGrid[0][j] == 0; j++) {
            if (obstacleGrid[0][j] == 1){
                break;
            }
            dp[0][j] = 1;
        }

        // 根据状态转移方程 dp[i][j] = dp[i - 1][j] + dp[i][j - 1] 进行递推。
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (obstacleGrid[i][j] == 0) {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
        }
        return dp[m - 1][n - 1];
    }

    public static void main(String[] args) {

        int[][] obstacleGrid = new int[][]{{0,0,0},
                                            {0,1,0},
                                             {0,0,0}};

        int[][] obstacleGrid1 = new int[][]{{1}};

        //int path1 = uniquePathsWithObstacles2(obstacleGrid1);
        int path = uniquePathsWithObstacles(obstacleGrid1);
        System.out.println(path);
       // System.out.println(path1);

        obstacleGrid1 = new int[][]{{1,0}};

        int path1 = uniquePathsWithObstacles2(obstacleGrid1);
        System.out.println(path1);

    }
}
