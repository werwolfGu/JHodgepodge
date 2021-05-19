package com.guce;

import com.guce.module.ListNode;

/**
 * @Author chengen.gce
 * @DATE 2020/6/26 10:28 下午
 * https://leetcode-cn.com/problems/remove-duplicates-from-sorted-list/
 * 给定一个排序链表，删除所有重复的元素，使得每个元素只出现一次。
 */
public class DeleteDuplicates {

    public static ListNode solution(ListNode head){

        if (head == null){
            return head;
        }
        ListNode slow = head ,fast = head.next;
        while (fast != null){
            if (slow.val != fast.val){
                slow.next = fast;
                slow = fast;
            }
            fast = fast.next;
        }
        slow.next = null;
        return head;
    }

    public static int[] solution(int[] nums){

        if (nums == null && nums.length <= 1){
            return nums;
        }

        int slow = 0 ;
        for (int i = 0 ; i < nums.length ; i++ ){
            if (nums[slow] != nums[i]){
               slow++;
            }
            nums[slow] = nums[i];
        }
        return nums;

    }


    public static void main(String[] args) {
        ListNode head = ListNode.createListNode(new int[]{1,1,2,2,3,3,3,3,4,4});
        ListNode result = solution(head);
        ListNode.printListNode(result);
        int[] res = solution(new int[]{1,1,2,2,3,3,3,3,4,4,5,6});
        for (int i = 0 ; i < res.length ; i++ ){
            System.out.print(res[i] + " ");
        }

    }
}
