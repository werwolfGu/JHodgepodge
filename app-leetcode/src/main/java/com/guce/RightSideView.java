package com.guce;

import com.guce.module.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @Author chengen.gce
 * @DATE 2020/4/25 10:57 上午
 * https://leetcode-cn.com/problems/binary-tree-right-side-view/
 * 二叉树的右视图  想象自己站在二叉树的右侧，按照从顶部到底部的顺序，看到的二叉树的那些节点；
 */
public class RightSideView {

    public static List<Integer> solution(TreeNode root){
        if (root == null){
            return null;
        }
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        List<Integer> res = new ArrayList<>();
        while (!queue.isEmpty()){
            int size = queue.size();
            for (int i = 0 ; i < size ; i++ ){
                TreeNode  node = queue.poll();
                if (i == 0){
                    res.add(node.getVal());
                }
                if (node.getRight() != null ){
                    queue.add(node.getRight());
                }
                if (node.getLeft() != null ){
                    queue.add(node.getLeft());
                }
            }

        }
        return res;
    }

    public static void main(String[] args) {
        TreeNode root = TreeNode.createBinaryTree(new Integer[]{1,2,3,4,5,6,7,8},0);

        System.out.println(solution(root));
    }
}
