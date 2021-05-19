package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/6/17 10:53 下午
 * https://leetcode-cn.com/problems/best-sightseeing-pair/
 * 最佳观光组合
 * <p>
 * 给定正整数数组 A，A[i] 表示第 i 个观光景点的评分，并且两个景点 i 和 j 之间的距离为 j - i。
 * <p>
 * 一对景点（i < j）组成的观光组合的得分为（A[i] + A[j] + i - j）：景点的评分之和减去它们两者之间的距离。
 * <p>
 * 返回一对观光景点能取得的最高分。
 * <p>
 * <p>
 * 示例：
 * <p>
 * 输入：[8,1,5,2,6]
 * 输出：11
 * 解释：i = 0, j = 2, A[i] + A[j] + i - j = 8 + 5 + 0 - 2 = 11
 */
public class MaxScoreSightseeingPair {

    public static int solution(int[] A) {
        int ans = 0, mx = A[0] + 0;
        for (int j = 1; j < A.length; ++j) {
            ans = Math.max(ans, mx + A[j] - j);
            // 边遍历边维护
            mx = Math.max(mx, A[j] + j);
        }
        return ans;

    }

    public static void main(String[] args) {
        System.out.println(solution(new int[]{3, 7, 2, 3}));
    }
}
