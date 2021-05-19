package com.guce;

/**
 * Created by chengen.gu on 2018/9/18.
 * https://leetcode-cn.com/problems/container-with-most-water/description/
 *
 *
 * 给你 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。在坐标内画 n 条垂直线，
 * 垂直线 i 的两个端点分别为 (i, ai) 和 (i, 0) 。找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
 *
 *
 *示例 1：
 *
 *
 *
 * 输入：[1,8,6,2,5,4,8,3,7]
 * 输出：49
 * 解释：图中垂直线代表输入数组 [1,8,6,2,5,4,8,3,7]。在此情况下，容器能够容纳水（表示为蓝色部分）的最大值为 49。
 *
 */
public class MaxArea {

    /**
     * 优化解法
     * @param height
     * @return
     */
    public static int solution(int[] height) {

        int maxArea = 0;

        int i = 0, j = height.length - 1;
        while (i < j) {
            int x = j - i;
            int y = Math.min(height[i], height[j]);
            maxArea = Math.max(maxArea, x * y);
            if (height[i] > height[j]) {
                j--;
            } else {
                i++;
            }
        }
        return maxArea;
    }

    public int maxArea(int[] height) {
        int maxArea = 0 ;
        for ( int i = 0 ; i < height.length -1; i++){
            for ( int j = i + 1 ; j < height.length ; j++ ){
                int x = j - i;
                int y = Math.min(height[i],height[j]);
                maxArea = Math.max(maxArea, x * y);
            }
        }
        return maxArea;
    }

    public static void main(String[] args) {
        int[] len = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        int area = solution(len);
        System.out.println(area);
    }
}
