package com.guce.module;

/**
 * Created by chengen.gu on 2018/10/10.
 */
public class ListNode {
    public int val;
    public ListNode next;
    public ListNode(int val){
        this.val = val;
    }

    public ListNode() {
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public ListNode getNext() {
        return next;
    }

    public void setNext(ListNode next) {
        this.next = next;
    }

    public static void printListNode(ListNode list) {
        ListNode node = list;
        while (node != null) {
            System.out.print(node.val + " ");
            node = node.next;

        }
        System.out.println(" ");
    }

    public static ListNode createListNode(int[] nums) {
        ListNode root = null, curr = null;
        for (int i = 0; i < nums.length; i++) {
            ListNode tmp = new ListNode(nums[i]);

            if (root == null) {
                root = tmp;
                curr = tmp;
                continue;
            }
            curr.next = tmp;
            curr = tmp;

        }
        return root;
    }
}
