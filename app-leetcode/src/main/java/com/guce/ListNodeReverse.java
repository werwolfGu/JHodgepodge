package com.guce;

import com.guce.node.ListNode;
import com.guce.node.ListNodeOpera;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

/**
 * @Author chengen.gu
 * @DATE 2020/2/9 2:57 下午
 * 单链表反转
 */
public class ListNodeReverse {


    public ListNode<Integer> reverse(ListNode<Integer> node){
        Stack<ListNode<Integer>> stack = new Stack<>();

        while (node != null ){
            stack.push(node);
            node = node.getNext();
        }

        ListNode<Integer> result = null,currNode = null,tmpNode = null;
        while (stack.size() > 0 && (currNode = stack.pop()) != null){
            currNode.setNext(null);
            if (result == null){
                result = currNode;
            }else {
                tmpNode.setNext(currNode);
            }
            tmpNode = currNode;

        }
        return result;
    }

    public ListNode reverseList(ListNode head) {

        Deque<ListNode> stack = new ArrayDeque<>();
        ListNode node = head;
        while (node != null) {
            stack.push(node);
            node = node.next;
        }

        ListNode result = null, currNode = null, tmpNode = null;
        while (!stack.isEmpty()) {

            currNode = stack.pop();
            if (result == null) {
                result = currNode;
                tmpNode = currNode;
                continue;
            }
            tmpNode.next = currNode;
            tmpNode = currNode;
            currNode.next = null;

        }
        return result;
    }


    /**
     * 时间复杂度 O(n) 空间复杂度 O(1)
     *
     * @param head
     * @return
     */
    public ListNode reverse1(ListNode head) {
        ListNode node = head, result = new ListNode(-1);
        while (node != null) {
            ListNode tmp = result.next;
            result.next = node;
            node = node.next;
            result.next.next = tmp;
        }

        return result.next;
    }


    public ListNode<Integer> listNodeReverse(ListNode<Integer> head, int m, int n) {


        ListNode<Integer> startNode = head;
        for (int i = 1; i < m; i++) {
            startNode = startNode.next;
        }

        ListNode<Integer> result = new ListNode<>(-1), currNode = startNode.next;

        ListNode node = null;
        for (int i = 0; i < n; i++) {
            node = result.next;
            result.next = currNode;
            currNode = currNode.next;
            result.next.next = node;
        }

        startNode.next.next = currNode;
        startNode.next = node;
        return head;
    }



    public static void main(String[] args) {
        ListNodeOpera opera = new ListNodeOpera();
        ListNode<Integer> heard = opera.addNode(4);
        ListNode<Integer> node = opera.addNode(heard,7);
        node = opera.addNode(node,8);
        node = opera.addNode(node,9);
        node = opera.addNode(node,1);
        node = opera.addNode(node, 5);
        node = opera.addNode(node, 6);

        opera.printListNode(heard);

        ListNodeReverse reverse = new ListNodeReverse();
        heard = reverse.reverse(heard);

        opera.printListNode(heard);

        heard = reverse.reverse(heard);

        opera.printListNode(heard);

        heard = reverse.reverse1(heard);
        opera.printListNode(heard);

        reverse.listNodeReverse(heard, 2, 4);
        //opera.printListNode(heard);

    }
}
