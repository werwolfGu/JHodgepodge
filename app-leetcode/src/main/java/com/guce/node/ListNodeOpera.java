package com.guce.node;

/**
 * @Author chengen.gu
 * @DATE 2020/2/9 2:58 下午
 */
public class ListNodeOpera {

    public <T> ListNode<T> addNode(T val){
        ListNode<T> node = new ListNode<>(val);
        return node;
    }

    public <T> ListNode<T> addNode(ListNode<T> preNode , T val){

        ListNode<T> node = new ListNode<>(val);
        preNode.setNext( node);
        return node;
    }

    public void printListNode(ListNode node){
        ListNode tmpNode = node;
        while ( tmpNode != null){
            System.out.print(tmpNode.val + " ");
            tmpNode = tmpNode.next;
        }
        System.out.println();
    }
}
