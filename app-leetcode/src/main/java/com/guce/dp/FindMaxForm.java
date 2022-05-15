package com.guce.dp;

/**
 * @Author chengen.gce
 * @DATE 2021/3/21 3:36 下午
 * https://leetcode-cn.com/problems/ones-and-zeroes/
 *
 * 给你一个二进制字符串数组 strs 和两个整数 m 和 n 。
 *
 * 请你找出并返回 strs 的最大子集的长度，该子集中 最多 有 m 个 0 和 n 个 1 。
 *
 * 如果 x 的所有元素也是 y 的元素，集合 x 是集合 y 的 子集 。
 *
 * 示例 1：
 *
 * 输入：strs = ["10", "0001", "111001", "1", "0"], m = 5, n = 3
 * 输出：4
 * 解释：最多有 5 个 0 和 3 个 1 的最大子集是 {"10","0001","1","0"} ，因此答案是 4 。
 * 其他满足题意但较小的子集包括 {"0001","1"} 和 {"10","1","0"} 。{"111001"} 不满足题意，因为它含 4 个 1 ，大于 n 的值 3 。
 * 示例 2：
 *
 * 输入：strs = ["10", "0", "1"], m = 1, n = 1
 * 输出：2
 * 解释：最大的子集是 {"0", "1"} ，所以答案是 2 。
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
