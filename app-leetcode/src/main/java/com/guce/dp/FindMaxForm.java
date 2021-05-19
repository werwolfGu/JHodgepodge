package com.guce.dp;

/**
 * @Author chengen.gce
 * @DATE 2021/3/21 3:36 下午
 * https://leetcode-cn.com/problems/ones-and-zeroes/
 */
public class FindMaxForm {

    public int findMaxForm(String[] strs, int m, int n) {
        int[][] dp = new int[m + 1][n + 1];

        for (String str : strs){
            int[] arr = str01(str);
            int S0 = arr[0];
            int S1 = arr[1];

            for (int i = m ; i >= S0 ;i--){
                for (int j = n ; j >= S1 ;j--){

                    dp[i][j] = Math.max(dp[i][j],dp[i - S0][j-S1] + 1);
                }
            }
        }

        return dp[m][n];
    }

    public int[] str01(String str){
        int[] arr = new int[2];
        for (int i = 0 ; i < str.length() ; i++){
            if (str.charAt(i) == '0'){
                arr[0] += 1;
            }
            if (str.charAt(i) == '1'){
                arr[1] += 1;
            }
        }
        return arr;
    }

    public static void main(String[] args) {

        FindMaxForm maxForm = new FindMaxForm();

        System.out.println(maxForm.findMaxForm(new String[]{"10", "0001", "111001", "1", "0"},5,3));
    }
}
