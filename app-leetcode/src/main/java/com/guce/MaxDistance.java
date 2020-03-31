package com.guce;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author chengen.gce
 * @DATE 2020/3/30 10:40 下午
 * https://leetcode-cn.com/problems/as-far-from-land-as-possible/
 *
 * 你现在手里有一份大小为 N x N 的『地图』（网格） grid，上面的每个『区域』（单元格）都用 0 和 1 标记好了。其中 0 代表海洋，1 代表陆地，你知道距离陆地区域最远的海洋区域是是哪一个吗？请返回该海洋区域到离它最近的陆地区域的距离。
 *
 */
public class MaxDistance {

    public int solution(int[][] grid){

        int[] dx = new int[]{0,0,1 ,-1};
        int[] dy = new int[]{1, -1,0,0};
        Queue<int[]> queue = new LinkedBlockingQueue();

        int lenX = grid.length , lenY = grid[0].length;
        for (int i = 0 ; i < grid.length ;i++){
            for (int j = 0 ; j < grid[i].length ;j++){
                if (grid[i][j] == 1){
                    queue.add(new int[]{i,j});
                }
            }
        }
        int[] points = null;
        boolean hasOcean = false;
        while (!queue.isEmpty()){
            points = queue.poll();
            int x = points[0];
            int y = points[1];
            for (int i = 0 ; i < 4 ; i++ ){
                int newX = points[0] + dx[i];
                int newY = points[1] + dy[i];
                if (x < 0 || x >lenX || y < 0 || y > lenY || grid[newX][newX] != 1){
                    continue;
                }
                grid[newX][newY] = grid[x][y] + 1;
                hasOcean = true;
                queue.add(new int[]{newX,newY});
            }
        }
        if (!hasOcean || points == null){
            return -1;
        }

        return grid[points[0]][points[1]] - 1;

    }

    public static void main(String[] args) {

    }
}
