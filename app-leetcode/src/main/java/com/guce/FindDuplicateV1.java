package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/5/26 9:16 下午
 * https://leetcode-cn.com/problems/find-the-duplicate-number/
 * https://leetcode-cn.com/problems/linked-list-cycle-ii/
 * 寻找重复数
 * 给定一个包含 n + 1 个整数的数组 nums，其数字都在 1 到 n 之间（包括 1 和 n），可知至少存在一个重复的整数。假设只有一个重复的整数，找出这个重复的数。
 *
 * 示例 1:
 *
 * 输入: [1,3,4,2,2]
 * 输出: 2
 */
public class FindDuplicateV1 {

    public static int solution(int[] nums){
        //快慢指针
        int slow = 0 , fast = 0 ;
        do{
            slow = nums[slow];
            fast = nums[nums[fast]];
        }while (slow != fast);
        slow = 0 ;
        while (slow != fast){
            slow = nums[slow];
            fast = nums[fast];
        }
        return slow;
    }

    public static void main(String[] args) {
        System.out.println(solution(new int[]{1,3,4,2,2}));
    }
}
