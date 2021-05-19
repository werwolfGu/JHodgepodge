package com.guce;

import com.guce.module.TreeNode;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @Author chengen.gce
 * @DATE 2020/9/16 9:17 下午
 *
 * https://leetcode-cn.com/problems/invert-binary-tree/
 *  翻转二叉树
 *      4
 *    /   \
 *   2     7
 *  / \   / \
 * 1   3 6   9
 *
 * 输出
 *
 *      4
 *    /   \
 *   7     2
 *  / \   / \
 * 9   6 3   1
 *
 */
public class InvertTree {

    public TreeNode invertTree(TreeNode root) {

        Deque<TreeNode> deque = new ArrayDeque<>();
        deque.add(root);
        while (!deque.isEmpty()){
            TreeNode node = deque.poll();
            TreeNode l = node.left;
            TreeNode r = node.right ;
            node.right = l;
            node.left = r;
            if (l != null){
                deque.add(l);
            }
            if (r != null){
                deque.add(r);
            }
        }
        return root;
    }
}
