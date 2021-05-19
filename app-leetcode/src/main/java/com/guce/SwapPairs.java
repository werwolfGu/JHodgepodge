package com.guce;

/**
 * Created by chengen.gu on 2019/10/26.
 * https://leetcode-cn.com/problems/swap-nodes-in-pairs/
 * 给定一个链表，两两交换其中相邻的节点，并返回交换后的链表。
 * 给定 1->2->3->4, 你应该返回 2->1->4->3.
 */
public class SwapPairs {

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }


    public ListNode swapPairs(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode result = head.next, curr = head, tmp, preNode = null;
        if (result == null) {
            return head;
        }
        while (curr != null) {
            ListNode next = curr.next;
            if (preNode != null) {
                preNode.next = next;
            }
            tmp = next.next;
            next.next = curr;
            curr.next = tmp;
            preNode = curr;
            curr = tmp;
        }
        return result;
    }

    public ListNode solution(ListNode head) {
        ListNode first, second, curr, result = null;
        curr = head;

        while (curr != null) {
            first = curr;
            second = first.next;
            if (result == null) {
                result = second;
            }
            if (second != null) {
                curr = second.next;
                second.next = first;
            }
            if (curr != null) {
                first.next = curr.next;

            } else {
                first.next = null;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode curr = new ListNode(2);
        head.next = curr;
        curr.next = new ListNode(3);
        curr = curr.next;
        curr.next = new ListNode(4);

        printListNode(head);
        System.out.println("===========");
        SwapPairs pairs = new SwapPairs();
        ListNode result = pairs.swapPairs(head);

        printListNode(result);


        ListNode head1 = new ListNode(1);
        ListNode curr1 = new ListNode(2);
        head1.next = curr1;
        curr1.next = new ListNode(3);
        curr1 = curr1.next;
        curr1.next = new ListNode(4);
        System.out.println("++++++++++");
        result = pairs.solution(head1);
        printListNode(result);

    }

    public static void printListNode(ListNode node) {
        ListNode curr = node;
        while (curr != null) {
            System.out.println(curr.val);
            curr = curr.next;
        }
    }
}
