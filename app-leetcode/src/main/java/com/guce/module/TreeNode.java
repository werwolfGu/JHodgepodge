package com.guce.module;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @Author chengen.gce
 * @DATE 2020/4/19 12:32 下午
 * <p>
 * 广度优先非递归用队列
 * 深度优先非递归用堆栈
 */
@Getter
@Setter
public class TreeNode {

    public int      val     ;
    public TreeNode left    ;
    public TreeNode right   ;

    public TreeNode(int val) {
        this.val = val;
    }

    public TreeNode() {
    }

    public static TreeNode createBinaryTree(Integer[] arr) {
        return createBinaryTree(arr, 0);
    }

    public static TreeNode createBinaryTree(Integer[] arr, int idx) {

        if (idx < arr.length) {
            if (arr[idx] != null){
                TreeNode node = new TreeNode(arr[idx]);
                TreeNode left = createBinaryTree(arr, 2 * idx + 1);
                TreeNode right = createBinaryTree(arr, 2 * idx + 2);

                node.left = left;
                node.right = right;
                return node;
            }
        }
        return null;

    }

    /**
     * 广度优先遍历
     *
     * @param node
     * @return
     */
    public static TreeNode bfs(TreeNode node) {

        if (node == null) {
            return null;
        }
        Queue<TreeNode> queue = new ArrayDeque<>();

        queue.add(node);
        while (!queue.isEmpty()) {
            TreeNode queueNode = queue.poll();
            System.out.println(queueNode.val);
            if (queueNode.left != null) {
                queue.add(queueNode.left);
            }
            if (queueNode.right != null) {
                queue.add(queueNode.right);
            }
        }

        return null;
    }

    public static TreeNode dfs(TreeNode node) {
        if (node == null) {
            return null;
        }
        System.out.println(node.val);
        if (node.left != null) {
            bfs(node.getLeft());

        }
        if (node.right != null) {
            bfs(node.getRight());

        }
        return null;

    }

    public static int treeHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return Math.max(treeHeight(node.getLeft()), treeHeight(node.getRight())) + 1;
    }

    /**
     * 非递归实现计算二叉树高度
     *
     * @param node
     * @return
     */
    public static int dfsHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }

        Queue<TreeNode> stack = new ArrayDeque<>();
        stack.add(node);
        int dept = 0;
        while (!stack.isEmpty()) {
            dept++;
            int size = stack.size();
            for (int i = 0; i < size; i++) {
                TreeNode treeNode = stack.poll();
                if (treeNode.left != null) {
                    stack.add(treeNode.left);
                }
                if (treeNode.right != null) {
                    stack.add(treeNode.right);
                }
            }
        }

        return dept;

    }

    public static void main(String[] args) {

        TreeNode node = createBinaryTree(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, 0);
        System.out.println(node);
        System.out.println(bfs(node));
        System.out.println(dfs(node));
        System.out.println(treeHeight(node));
        System.out.println(dfsHeight(node));
    }
}
