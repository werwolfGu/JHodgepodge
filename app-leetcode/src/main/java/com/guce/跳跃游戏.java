package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2021/5/23 3:31 下午
 */
public class 跳跃游戏 {

    /**
     * https://leetcode-cn.com/problems/jump-game/
     * 给定一个非负整数数组 nums ，你最初位于数组的 第一个下标 。
     *
     * 数组中的每个元素代表你在该位置可以跳跃的最大长度。
     *
     * 判断你是否能够到达最后一个下标。
     *
     * @param nums
     * @return
     */
    public boolean jump(int[] nums) {

        int position = 0;
        for (int i = 0 ; i < nums.length; i++){
            if (i > position){
                return false;
            }
            position = Math.max(position, i+ nums[i]);
        }
        return true;
    }

    /**
     * https://leetcode-cn.com/problems/jump-game-ii/
     * @param nums
     * @return
     */
    public int jump2 (int[] nums) {
        int maxPosition = 0 ;
        int end = 0 , step = 0;
        for (int i = 0 ; i < nums.length; i++ ){
            maxPosition = Math.max(maxPosition , nums[i] + i);
            if (i == end) {
                end = maxPosition;
                step++;
            }
        }
        return step;
    }

    public static void main(String[] args) {
        int x = -11;
        int mod = x / 10 ;
        System.out.println(mod);
        System.out.println(Integer.MAX_VALUE);
        
    }
}
