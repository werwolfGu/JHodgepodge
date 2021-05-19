package com.guce.listnode;


import com.guce.module.ListNode;

/**
 * @Author chengen.gce
 * @DATE 2021/3/17 2:03 下午
 */
public class ListNodeReverse {

    /**
     * 单链表反转  要求 时间复杂度 O(n) 空间复杂度 O(1)
     * @param head
     * @return
     */
    public static ListNode  listNodeReverse(ListNode head){

        ListNode res = new ListNode(-1) , curr = head;

        while (curr != null){
            ListNode tmp = curr;
            curr = curr.next;

            tmp.next = res.next  ;
            res.next = tmp;
        }
        return res.next;
    }

    /**
     * 单链表 m,n段反转
     * @param head
     * @param left
     * @param right
     * @return
     */
    public static ListNode listNodeeverseBetween(ListNode head,int left ,int right){

        ListNode resNode = new ListNode(-1);
        resNode.next = head;
        ListNode preNode = resNode;

        ////找到左节点的开始位置，但是要前一位
        for (int i = 0 ; i < left - 1 ; i++ ){
            preNode = preNode.next;
        }

        ////找到右节点
        ListNode rightNode = preNode;
        for (int i = 0 ; i < right -left + 1 ; i++ ){
            rightNode = rightNode.next;
        }


        ListNode leftNode = preNode.next;
        ////左节点切断
        preNode.next = null;

        ListNode postNode = rightNode.next;

        ////右节点切断
        rightNode.next = null;

        ////m-n 节点反转
        ListNode reverseNode = new ListNode(-1);
        while (leftNode != null){
            ListNode node = leftNode;
            leftNode = leftNode.next ;
            node.next = reverseNode.next;
            reverseNode.next = node;
        }

        ////左节点接上
        preNode.next = reverseNode.next;

        ListNode node = reverseNode ;
        while (node.next != null){
            node = node.next;
        }

        //// 又节点接上
        node.next = postNode;

        return resNode.next;
    }

    public static ListNode reverseKNode (ListNode head,int k){

        int size = 0 ;
        ListNode node = head;
        while (node != null){
            size++ ;
            node = node.next;
        }

        int ck = size / k;
        ListNode reverseHead = new ListNode(0) ,preNode = reverseHead;
        node = head;
        while (ck > 0){
            ck-- ;
            int idx = k;
            while ( idx > 0){
                idx-- ;
                ListNode tmp = node;
                node = node.next;
                tmp.next = preNode.next;
                preNode.next = tmp;
            }
            while (preNode.next != null){
                preNode = preNode.next;
            }
        }
        preNode.next = node;

        return reverseHead.next;

    }


    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode node = new ListNode(2);
        head.next = node;
        node.next = new ListNode(3);
        node = node.next;
        node.next = new ListNode(4);
        node = node.next;
        node.next = new ListNode(5);
        node = node.next;
        node.next = new ListNode(6);
        node = node.next;
        node.next = new ListNode(7);
        node = node.next;
        node.next = new ListNode(8);
        node = node.next;
        node.next = new ListNode(9);
        node = node.next;

        ListNode.printListNode(head);

        node = listNodeReverse(head);

        ListNode.printListNode(node);


        node = listNodeeverseBetween(node,2,5);
        ListNode.printListNode(node);

        node = reverseKNode(node,3);
        ListNode.printListNode(node);

    }
}
