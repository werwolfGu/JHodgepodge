package com.guce.dandiaoStack;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @Author chengen.gce
 * @DATE 2020/5/30 11:36 下午
 * https://leetcode-cn.com/problems/largest-rectangle-in-histogram/
 * 柱状图中最大的矩形 单调栈
 */
public class LargestRectangleArea {
    /**
     * 从中心点向2边扩张；
     * @param heights
     * @return
     */
    public static int solution (int[] heights){
        int max = 0;

        for (int i = 1 ; i < heights.length ; i++ ){
            int height = heights[i];
            int l = i ;
            while ( l > 0 && heights[l -1] >= height){
                l--;
            }
            int r = i;
            while (r < (heights.length - 1) && heights[r + 1] >= height){
                r++ ;
            }

            max = Math.max(max,(r - l + 1 ) * height);

        }
        return max;
    }


    /**
     * 单调递增栈
     * @param heights
     * @return
     */
    public  static int solution2(int[] heights){

        int len = heights.length;
        int res = 0;

        int[] newHeights = new int[len + 2];
        newHeights[0] = 0;
        System.arraycopy(heights, 0, newHeights, 1, len);
        newHeights[len + 1] = 0;
        len = len + 2;

        Deque<Integer> stack = new ArrayDeque<Integer>();
        stack.addLast(0);

        for (int i = 0 ; i < len ; i++ ){
            // 这个 while 很关键，因为有可能不止一个柱形的最大宽度可以被计算出来
            while (!stack.isEmpty() && newHeights[i] < newHeights[stack.peekLast()]) {
                int curHeight = newHeights[stack.pollLast()];
                while (!stack.isEmpty() && newHeights[stack.peekLast()] == curHeight) {
                    stack.pollLast();
                }

                int curWidth;
                if (stack.isEmpty()) {
                    curWidth = i;
                } else {
                    curWidth = i - stack.peekLast() - 1;
                }
                res = Math.max(res, curHeight * curWidth);
            }
            stack.addLast(i);

        }

        return res;

    }

    /**
     *
     * @param heights
     * @return
     */
    public static int solution3(int[] heights){

        Deque<Integer> stock = new ArrayDeque<>();

        int res = 0 ;
        int currHeight = 0 ,currWidth = 0 ;
        for (int i = 0 ; i < heights.length ; i++ ){
            while ( !stock.isEmpty() && heights[stock.peekLast()] > heights[i]){

                currWidth = stock.pollLast();
                currHeight = heights[currWidth];

                while ( !stock.isEmpty() && currHeight == heights[stock.peekLast()]){
                    currWidth = stock.pollLast();
                }

                res = Math.max(res,(i - currWidth - 1) * currHeight);
            }

            res = Math.max(res,(i - currWidth - 1) * currHeight);
            stock.addLast(i);
        }

        int len = heights.length;
        while ( !stock.isEmpty()){

            currHeight = heights[stock.pollLast()];
            while (!stock.isEmpty() && heights[stock.peekLast()] == currHeight) {
                stock.pollLast();
            }
            int curWidth;
            if (stock.isEmpty()) {
                curWidth = len;
            } else {
                curWidth = len - stock.peekLast() - 1;
            }
            res = Math.max(res, currHeight * curWidth);
        }

        res = Math.max(res,(heights.length - currWidth) * currHeight);
        return res;
    }
    public static void main(String[] args) {
        System.out.println(solution(new int[]{2,1,5,6,2,3,1,1,1,2,3,2,1}));
        System.out.println(solution2(new int[]{2,1,5,6,2,3,1,1,1,2,3,2,1}));
        System.out.println(solution3(new int[]{2,1,5,6,2,3,1,1,1,2,3,2,1}));
    }
}
