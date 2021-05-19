package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/4/21 9:40 下午
 * https://leetcode-cn.com/problems/count-number-of-nice-subarrays/
 * 给你一个整数数组 nums 和一个整数 k。
 *
 * 如果某个 连续 子数组中恰好有 k 个奇数数字，我们就认为这个子数组是「优美子数组」。
 *
 * 请返回这个数组中「优美子数组」的数目。
 *输入：nums = [1,1,2,1,1], k = 3
 * 输出：2
 * 解释：包含 3 个奇数的子数组是 [1,1,2,1] 和 [1,2,1,1] 。
 *
 * 输入：nums = [2,2,2,1,2,2,1,2,2,2], k = 2
 * 输出：16
 */
public class NumberOfSubarrays {

    public static int solution(int[] nums, int k){

       int res = 0,len = nums.length;
       int x ;
       for (int i = 0 ; i < len - k + 1; i++ ){
           x = 0;
           for (int j = i ; j < len ; j++ ){
               if ( (nums[j] & 1) == 1){
                   x++ ;
               }
               if (x > k){
                   break;
               }
               if (x == k){
                   res++ ;
               }
           }
       }
       return res;
    }

    public static int solution2(int[] nums ,int k){
        int left = 0 , right = 0 , count = 0,res = 0;

        while ( right < nums.length){
            //右指针向后走
            if((nums[right++] & 1) == 1){
                count++;
            }

            if (count == k){
                int rightcount = 0 ;
                //右指针向→走 直到发现有基数位置
                while(right < nums.length && (nums[right++] & 1) == 0){
                    rightcount++ ;
                }

                int leftcount = 0 ;
                while ( (nums[left++] & 1) == 0){
                    leftcount++ ;
                }
                // 第 1 个奇数左边的 leftEvenCnt 个偶数都可以作为优美子数组的起点
                // (因为第1个奇数左边可以1个偶数都不取，所以起点的选择有 leftcount + 1 种）
                // 第 k 个奇数右边的 rightEvenCnt 个偶数都可以作为优美子数组的终点
                // (因为第k个奇数右边可以1个偶数都不取，所以终点的选择有 rightcount + 1 种）
                // 所以该滑动窗口中，优美子数组左右起点的选择组合数为 (rightcount + 1) * (leftcount + 1)
                res += (rightcount +1) * (leftcount + 1);
                // 此时 left 指向的是第 1 个奇数，因为该区间已经统计完了，因此 left 右移一位，count--
                left++ ;
                count-- ;
            }
        }
        return res;
    }


    public static void main(String[] args) {
        System.out.println(solution(new int[]{2,2,2,1,2,2,1,2,2,2},2));
        System.out.println(solution2(new int[]{2,2,2,1,2,2,1,2,2,2},2));
    }
}
