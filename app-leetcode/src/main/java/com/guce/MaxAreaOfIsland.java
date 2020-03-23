package com.guce;

import java.util.Stack;

/**
 * @Author chengen.gce
 * @DATE 2020/3/15 6:54 下午
 * https://leetcode-cn.com/problems/max-area-of-island/
 * <p>
 * 给定一个包含了一些 0 和 1的非空二维数组 grid , 一个 岛屿 是由四个方向 (水平或垂直) 的 1 (代表土地) 构成的组合。你可以假设二维矩阵的四个边缘都被水包围着。
 * <p>
 * 找到给定的二维数组中最大的岛屿面积。(如果没有岛屿，则返回面积为0。)
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/max-area-of-island
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class MaxAreaOfIsland {
    /**
     * 递归方法实现
     *
     * @param grid
     * @return
     */
    public static int solution(int[][] grid) {

        int currMaxArea = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int tmp = maxArea(i, j, grid);
                currMaxArea = Math.max(currMaxArea, tmp);
            }
        }
        return currMaxArea;
    }

    public static int maxArea(int i, int j, int[][] grid) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[i].length || grid[i][j] == 0) {
            return 0;
        }
        grid[i][j] = 0;
        int up = maxArea(i - 1, j, grid);
        int down = maxArea(i + 1, j, grid);
        int left = maxArea(i, j - 1, grid);
        int right = maxArea(i, j + 1, grid);
        return up + down + left + right + 1;
    }


    public static int solutionUseStack(int[][] grid){
        int maxArea = 0 ;
        Stack<int[]> stack = new Stack<>();
        for (int i = 0 ; i < grid.length ; i++ ){

            for (int j = 0 ; j < grid[i].length ; j++ ){
                stack.add(new int[]{i,j});
                int currMaxArea = 0;
                while (!stack.isEmpty()){
                    int[] value = stack.pop();
                    int currI = value[0];
                    int currJ = value[1];
                    if ( currI < 0 || currI >= grid.length || currJ < 0
                            || currJ >= grid[currI].length || grid[currI][currJ] == 0){
                        continue;
                    }
                    currMaxArea++ ;
                    grid[currI][currJ] = 0;
                    stack.add(new int[]{currI - 1,currJ});
                    stack.add(new int[]{currI + 1,currJ});
                    stack.add(new int[]{currI ,currJ - 1});
                    stack.add(new int[]{currI ,currJ + 1});

                }

                maxArea = Math.max(maxArea,currMaxArea);
            }
        }
        return maxArea;
    }

    public static void main(String[] args) {
        int[][] grid = {
                {0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0},
                {0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0}
        };

       // System.out.println(solution(grid));
        System.out.println(solutionUseStack(grid));
    }
}
