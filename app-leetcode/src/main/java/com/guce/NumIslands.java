package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/3/15 7:50 下午
 * https://leetcode-cn.com/problems/number-of-islands/
 */
public class NumIslands {

    public static int solution(int[][] grid){
        int num = 0;

        for (int i = 0 ; i < grid.length ; i++ ){

            for (int j = 0 ;  j < grid[i].length ; j++ ){
                if ( grid[i][j] == 1){
                      num++ ;
                      dfs(i,j,grid);
                }
            }
        }
        return num;
    }

    public static void dfs(int i , int j ,int[][] grid){

        if (i < 0 || i >= grid.length || j < 0 || j >= grid[i].length || grid[i][j] == 0){
            return ;
        }
        grid[i][j] = 0 ;

        dfs(i - 1, j, grid);
        dfs(i + 1, j, grid);
        dfs(i, j - 1, grid);
        dfs(i, j + 1, grid);
    }

    public static void main(String[] args) {
        int[][] grid = {{1,1,1,1,0},
                        {1,1,0,1,0},
                        {1,1,0,0,0},
                        {0,0,0,0,0}};

        int[][] grid1 = {{1,1,0,0,0},
                         {1,1,0,0,0},
                         {0,0,1,0,0},
                         {0,0,0,1,1}} ;
        System.out.println(solution(grid));
        System.out.println(solution(grid1));
    }
}
