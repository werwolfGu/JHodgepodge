package com.guce.tree;

import com.guce.module.TreeNode;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @Author chengen.gce
 * @DATE 2020/9/24 9:51 下午
 * https://leetcode-cn.com/problems/minimum-depth-of-binary-tree/
 *
 * 给定一个二叉树，找出其最小深度。
 *
 * 最小深度是从根节点到最近叶子节点的最短路径上的节点数量。
 *
 * 说明: 叶子节点是指没有子节点的节点。
 *
 * 示例:
 *
 * 给定二叉树 [3,9,20,null,null,15,7],
 *
 *     3
 *    / \
 *   9  20
 *     /  \
 *    15   7
 * 返回它的最小深度  2.
 *
 */
public class MinDepth {

    /**
     * 广度优先遍历
     * @param root
     * @return
     */
    public static int minDepth(TreeNode root) {
        if (root == null){
            return 0 ;
        }
        int depth = 0;
        Deque<TreeNode> deque = new ArrayDeque<>();
        deque.add(root);
        while(!deque.isEmpty()){
            int size = deque.size();
            depth++ ;

            for (int i = 0 ; i < size ; i++ ){
                TreeNode node = deque.poll();
                if (node.left == null && node.right == null){
                    return depth;
                }
                if (node.left != null){
                    deque.add(node.left);
                }
                if (node.right != null){
                    deque.add(node.right);
                }
            }
        }
        return depth;
    }

    /**
     * 深度优先遍历
     * @param root
     * @return
     */
    public static int minDepthDfs(TreeNode root) {

        if (root == null){
            return 0;
        }

        if (root.left == null && root.right == null){
            return 1;
        }
        int depth = Integer.MAX_VALUE;

        if (root.left != null){
            depth = Math.min(minDepthDfs(root.left),depth) + 1;
        }
        if (root.right != null){
            depth = Math.min(minDepthDfs(root.right),depth) + 1;
        }
        return depth;
    }

    public static void main(String[] args) {
        TreeNode node = TreeNode.createBinaryTree(new Integer[]{1,2,3,4,null,null,5},0);
        System.out.println(minDepth(node));

        System.out.println(minDepthDfs(node));
    }

}
