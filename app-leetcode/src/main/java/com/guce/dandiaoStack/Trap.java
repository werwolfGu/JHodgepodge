package com.guce.dandiaoStack;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @Author chengen.gce
 * @DATE 2020/4/18 11:26 下午
 * https://leetcode-cn.com/problems/trapping-rain-water/
 * 接雨水
 */
public class Trap {

    public static int solution(int[] height){
        int maxHeight = maxHeight(height);

        int sum = 0 ;
        for (int i = 1 ; i <= maxHeight ; i++){
            int tmp = 0 ;
            boolean isStart = false;
            for (int j = 0 ; j < height.length ;j++ ){
                if (height[j] >= i){
                    sum += tmp;
                    tmp = 0;
                    isStart = true;
                }
                if (isStart && height[j] < i){
                    tmp += 1;
                }
            }
        }
        return sum;
    }

    public static int maxHeight(int[] height){
        int max = 0 ;
        for (int i = 0 ; i < height.length; i++ ){
            max = Math.max(max,height[i]);
        }
        return max;
    }

    /**
     * 求出当前列能接到的雨水的数量，我们只需要算出左边最高的墙和右边最高的墙 的最小值 减去当前的列的高度就可以了
     * @param height
     * @return
     */
    public static int solution2(int[] height){

        int res = 0 ;
        for (int i = 1 ; i < height.length -1  ; i++ ){
            int maxLeft = 0;
            int maxRight = 0 ;
            //计算出左边最高的墙
            for (int j = 0 ; j < i ; j++ ){
                maxLeft = Math.max(maxLeft,height[j]);
            }
            //计算出右边最高的墙
            for (int j = i+ 1; j < height.length ; j++){
                maxRight = Math.max(maxRight,height[j]);
            }
            int min =  Math.min(maxLeft,maxRight);
            //当前列墙能接收到的雨水高度
            if (min > height[i]){
                res = res + min - height[i];
            }

        }
        return res;
    }

    /**
     * 单调递减栈
     * @param height
     * @return
     */
    public static int solution3(int[] height){

        Deque<Integer> stack = new ArrayDeque<>();
        int res = 0;
        for (int i = 0 ; i < height.length ;i++ ){

            while ( !stack.isEmpty() && height[i] > height[stack.peekLast()]){

                int x  = stack.pollLast();
                if (stack .isEmpty()){
                    break;
                }

                //两堵墙之前的距离。
                int distance = i - stack.peekLast() - 1;

                int currHeight = Math.min(height[i],height[stack.peekLast()]) - height[x];

                res += currHeight * distance;
            }
            stack.addLast(i);
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(solution(new int[]{0,1,0,2,1,0,1,3,2,1,2,1}));
        System.out.println(solution2(new int[]{0,1,0,2,1,0,1,3,2,1,2,1}));
        System.out.println(solution3(new int[]{0,1,0,2,1,0,1,3,2,1,2,1}));
        System.out.println(solution3(new int[]{4,2,0,3,2,5}));
    }
}
