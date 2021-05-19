package com.guce;

/**
 * @Author chengen.gu
 * @DATE 2020/2/9 2:26 下午
 * https://leetcode-cn.com/problems/add-two-numbers/
 * 求两数相加
 */
public class AddTwoNumbers {

    public static class ListNode{
        int val;
        ListNode next;
        public ListNode(int x){
            this.val = x;
        }
    }

    public ListNode solution(ListNode l1,ListNode l2){
        ListNode result = null,currNode = null;
        int multi = 0 ;
        while ( l1 != null || l2 != null){
            int val1 = l1 != null ? l1.val : 0;
            int val2 = l2 != null ? l2.val : 0;
            int val = val1 + val2 + multi;
            multi = 0;
            if (val >= 10) {
                multi = 1;
                val = val % 10;
            }
            ListNode node = new ListNode(val);
            if (result == null){
                result = node;
            }else {
                currNode.next = node;
            }
            currNode = node;
            l1 = l1 == null ? null: l1.next;
            l2 = l2 == null ? null: l2.next;

        }
        if (multi > 0){
            ListNode node = new ListNode(multi);
            currNode.next = node;
        }

        return result;
    }
}
