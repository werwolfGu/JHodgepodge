package com.guce;

import com.guce.node.ListNode;
import com.guce.node.ListNodeOpera;

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



    public static void main(String[] args) {
        ListNodeOpera opera = new ListNodeOpera();
        ListNode<Integer> heard = opera.addNode(4);
        ListNode<Integer> node = opera.addNode(heard,7);
        node = opera.addNode(node,8);
        node = opera.addNode(node,9);
        node = opera.addNode(node,1);

        opera.printListNode(heard);

        ListNodeReverse reverse = new ListNodeReverse();
        heard = reverse.reverse(heard);

        opera.printListNode(heard);

        heard = reverse.reverse(heard);

        opera.printListNode(heard);
    }
}
