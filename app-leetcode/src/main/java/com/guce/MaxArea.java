package com.guce;

/**
 * Created by chengen.gu on 2018/9/18.
 * https://leetcode-cn.com/problems/container-with-most-water/description/
 */
public class MaxArea {

    public static int solution(int[] height){

        int maxArea = 0 ;
        /*for(int i = 0 ; i < height.length - 1 ; i++ ){

            for(int j = i + 1 ; j < height.length ; j++ ){

                int  x = j - i;
                int y = Math.min(height[i],height[j]);
                maxArea = Math.max(maxArea,x * y);
            }
        }*/

        int i = 0 ,j = height.length - 1;
        while (i < j){
            int x = j - i;
            int y = Math.min(height[i],height[j]);
            maxArea = Math.max(maxArea , x * y);
            if(height[i] > height[j]){
                j--;
            }else{
                i++;
            }
        }
        return maxArea;
    }
}
