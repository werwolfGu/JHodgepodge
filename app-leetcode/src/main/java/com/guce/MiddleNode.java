package com.guce;

import com.guce.module.ListNode;

/**
 * @Author chengen.gce
 * @DATE 2020/3/23 8:36 下午
 * https://leetcode-cn.com/problems/middle-of-the-linked-list/
 * 找出链表中的中间链表值
 */
public class MiddleNode {

    public static ListNode solution(ListNode head){
        ListNode pre = head;
        ListNode curr = head;
        if (head == null){
            return null;
        }
        while (curr != null){

            curr = curr.getNext();

            if (curr != null){
                pre = pre.getNext();
                curr = curr.getNext();
            }else {
                break;
            }

        }
        return pre;
    }

    public static void main(String[] args) {
        ListNode node = new ListNode(2);
        ListNode head = node;
        node.setNext(new ListNode(3));
        node = node.getNext();
        node.setNext(new ListNode(4));
        node = node.getNext();
        node.setNext(new ListNode(5));
        node = node.getNext();
        node.setNext(new ListNode(6));
        node = node.getNext();
        node.setNext(new ListNode(7));
        node = node.getNext();
       // node.setNext(new ListNode(8));

        ListNode res = solution(head);
        System.out.println(res.getVal());
    }
}
