package com.guce;

/**
 * Created by chengen.gu on 2018/9/18.
 * https://leetcode-cn.com/problems/container-with-most-water/description/
 */
public class MaxArea {

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

    public static void main(String[] args) {
        int[] len = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        int area = solution(len);
        System.out.println(area);
    }
}
