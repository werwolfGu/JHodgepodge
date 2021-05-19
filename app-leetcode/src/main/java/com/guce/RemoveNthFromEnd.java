package com.guce;

import com.guce.module.ListNode;

/**
 * @Author chengen.gce
 * @DATE 2020/4/30 9:51 下午
 * https://leetcode-cn.com/problems/remove-nth-node-from-end-of-list/
 * 删除链表的倒数第N个节点
 */
public class RemoveNthFromEnd {

    public static ListNode solution(ListNode head,int n){
        ListNode result = new ListNode();
        if (head == null){
            return result;
        }
        ListNode point = head;

        result.next = head;

        int idx = 0 ,len = 0;
        while (point != null){
            len++ ;
            point = point.next;

        }
        idx = len - n;
        if (idx >= 0){

            point = result;
            for (int i = 0 ; i < idx ; i++){
                point = point.next;
            }
            point.next = point.next.next;
        }


        return result.next;
    }

    public static void main(String[] args) {
        ListNode node = ListNode.createListNode(new int[]{1,2,3,4,5});
        ListNode res = solution(node,2);
        ListNode.printListNode(res);
    }
}
