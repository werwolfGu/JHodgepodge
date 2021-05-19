package com.guce;

import com.guce.module.ListNode;

/**
 * @Author chengen.gce
 * @DATE 2020/4/26 9:29 下午
 * https://leetcode-cn.com/problems/merge-k-sorted-lists/
 * 合并k个排序列表
 * 使用归并排序
 */
public class MergeKLists {


    public static ListNode solution(ListNode[] lists){

        ListNode node = new ListNode();

        return node;

    }

    public static ListNode merge2ListNode(ListNode node1 ,ListNode node2){

        ListNode node = new ListNode(),curr = node;
        while(node1 != null && node2 != null){

            if (node1.val > node2.val){
                curr.next  = node2;
                node2 = node2.next;
            }else{
                curr.next  = node1 ;
                node1 = node1.next;
            }
            curr = curr.next;

        }
        if (node1 != null){
            curr.next = node1;
        }

        if (node2 != null){
            curr.next = node2;
        }

        return node.next;

    }


    public static ListNode merge (ListNode[] list,int l,int r){
        if (l == r){
            return list[l];
        }
        if(l > r){
            return null;
        }
        int mid = (l +r ) >> 1;
        return merge2ListNode(merge(list,l,mid),merge(list,mid,r));
    }

    public static ListNode mergeKList(ListNode[] list){
        return merge(list,0,list.length -1);
    }

    public static void main(String[] args) {
        ListNode node1 = ListNode.createListNode(new int[]{1,3,4,5,8});
        ListNode node2 = ListNode.createListNode(new int[]{1,2,6,7,10});
        ListNode root = merge2ListNode(node1,node2);
        ListNode.printListNode(root);
    }
}
