package com.guce;

import com.guce.module.ListNode;

/**
 * Created by chengen.gu on 2018/10/10.
 */
public class MergeTwoLists {

    public static ListNode solution(ListNode l1,ListNode l2){

        ListNode result = null;
        ListNode currNode = null;
        while(l1 != null && l2 != null){
            ListNode node ;
            if(l1.getVal() > l2.getVal()){
                node = new ListNode(l2.getVal());
                l2 = l2.getNext();
            }else{
                node = new ListNode(l1.getVal());
                l1 = l1.getNext();
            }
            if(result == null){
                result = node;
            }else{
                currNode.setNext(node);
            }
            currNode = node;

        }
        while(l1 != null){
            ListNode node = new ListNode(l1.getVal());
            if(result == null){
                result = node;
            }else{
                currNode.setNext(node);
            }
            currNode = node;
            l1 = l1.getNext();
        }
        while(l2 != null){
            ListNode node = new ListNode(l2.getVal());
            if(result == null){
                result = node;
            }else{
                currNode.setNext(node);
            }
            currNode = node;
            l2 = l2.getNext();
        }

        return result;
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
