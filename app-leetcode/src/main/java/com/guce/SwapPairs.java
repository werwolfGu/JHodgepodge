package com.guce;

/**
 * Created by chengen.gu on 2019/10/26.
 */
public class SwapPairs {

    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }


    public ListNode swapPairs(ListNode head) {
        if (head == null){
            return null;
        }
        ListNode result = head.next, curr = head,tmp,preNode = null;
        if (result == null){
            return head;
        }
        while ( curr != null){
            ListNode next = curr.next;
            if (preNode != null){
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
    }

    public static void printListNode(ListNode node){
        ListNode curr = node ;
        while (curr != null){
            System.out.println(curr.val);
            curr = curr.next;
        }
    }
}
