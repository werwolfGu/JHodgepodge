package com.guce;

import com.guce.module.ListNode;

/**
 * Created by chengen.gu on 2018/10/10.
 */
public class MergeTwoLists {

    public static ListNode solution(ListNode l1,ListNode l2){

        ListNode result = new ListNode(0), curr;
        curr = result;
        while(l1 != null && l2 != null){
            int x = l1.val;
            int y = l2.val;
            if (x > y) {
                curr.next = l2;
                l2 = l2.next;
            } else {
                curr.next = l1;
                l1 = l1.next;
            }
            curr = curr.next;
        }
        if (l1 != null) {
            curr.next = l1;
        }
        if (l2 != null) {
            curr.next = l2;
        }
        return result.next;
    }

    public static void main(String[] args) {
        int a[] = new int[]{1,2,4};
        int b[] = new int[]{1,3,4};
        ListNode l1 = builderListNode(a);
        ListNode l2 = builderListNode(b);

        printListNode(solution(l1,l2));
    }

    public static void printListNode(ListNode listNode){
        ListNode node = listNode;
        while (node != null){
            System.out.print(node.getVal() + "->");
            node = node.next;
        }
    }

    public static ListNode builderListNode(int[] arr){
        ListNode listNode = null ;

        ListNode currNode = null;
        for(int i = 0 ; i < arr.length ; i++ ){
            ListNode node = new ListNode(arr[i]);
            if(listNode == null){
                listNode = node;
                currNode = node;
            }else{

                currNode.setNext(node);
                currNode = node;
            }
        }
        return listNode;
    }
}
