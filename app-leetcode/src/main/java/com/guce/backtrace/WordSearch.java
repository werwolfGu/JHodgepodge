package com.guce.backtrace;

/**
 * @Author chengen.gce
 * @DATE 2020/9/13 4:53 下
 * https://leetcode-cn.com/problems/word-search/
 * 单词搜索
 */
public class WordSearch {

    private static int[][] direction = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    private static boolean[][] used;

    static int m ;
    static int n ;
    public static boolean exist(char[][] board, String word) {

         m = board.length ;
         n = board[0].length;
        used = new boolean[m][n];
        for(int i = 0 ; i < m ; i++ ){
             for (int j = 0 ; j < n ; j++){
                 if (dfs(i, j, 0,word,board)) {
                     return true;
                 }
             }
        }
        return false;
    }


    public static boolean dfs(int i ,int j ,int start , String word,char[][] board){

        if (start == word.length() -1 ){
            return word.charAt(start) == board[i][j];
        }

        if (board[i][j] == word.charAt(start)) {
            used[i][j] = true;
            for (int k = 0; k < 4; k++) {
                int newX = i + direction[k][0];
                int newY = j + direction[k][1];

                if (inArea(newX,newY) && !used[newX][newY]){
                    if (dfs(newX,newY,start + 1,word,board)){
                        return true;
                    }
                }
            }
            used[i][j] = false;
        }
        return false;
    }

    private static boolean inArea(int x, int y) {
        return x >= 0 && x < m && y >= 0 && y < n;
    }





}
