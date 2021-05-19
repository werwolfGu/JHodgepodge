package com.guce.tree;

import com.guce.module.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2020/9/22 10:19 下午
 * https://leetcode-cn.com/problems/binary-tree-level-order-traversal-ii/
 *
 * 107. 二叉树的层次遍历 II
 * 给定一个二叉树，返回其节点值自底向上的层次遍历。 （即按从叶子节点所在层到根节点所在的层，逐层从左向右遍历）
 *
 * 例如：
 * 给定二叉树 [3,9,20,null,null,15,7],
 *
 *     3
 *    / \
 *   9  20
 *     /  \
 *    15   7
 * 返回其自底向上的层次遍历为：
 *
 * [
 *   [15,7],
 *   [9,20],
 *   [3]
 * ]
 */
public class LevelOrderBottom {

    public static List<List<Integer>> levelOrderBottom(TreeNode root) {

        List<List<Integer>> result = new ArrayList<>();
        Deque<TreeNode> queue = new ArrayDeque<>();

        queue.add(root);
        while(!queue.isEmpty()){

            int size = queue.size();
            List<Integer> list = new ArrayList<>();
            for (int i = 0 ; i < size ; i++ ){
                TreeNode node = queue.poll();
                list.add(node.val);
                if (node.left != null){
                    queue.add(node.left);

                }
                if (node.right != null){
                    queue.add(node.right);
                }

            }
            result.add(0,list);
        }
        return result;
    }

    public static void main(String[] args) {

        TreeNode root = TreeNode.createBinaryTree(new Integer[]{1,2,3,4,5,6,7,8,9},0);

        System.out.println(levelOrderBottom(root));
    }
}
