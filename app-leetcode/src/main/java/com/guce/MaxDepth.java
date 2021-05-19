package com.guce;

import com.guce.module.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @Author chengen.gce
 * @DATE 2020/4/19 12:31 下午
 * https://leetcode-cn.com/problems/er-cha-shu-de-shen-du-lcof/
 * 二叉树的深度
 */
public class MaxDepth {

    public static int solution(TreeNode root){
        if (root == null){
            return 0;
        }
        return depth(root);

    }

    /**
     * 递归深度遍历二叉树
     * @param node
     * @return
     */
    public static int depth(TreeNode node){
        if (node == null){
            return 0;
        }
        if ( node.getLeft() == null && node.getRight() == null){
            return 1;
        }

        return Math.max(depth(node.getLeft()),depth(node.getRight())) + 1;
    }

    //非递归 层次遍历
    public static int solution2(TreeNode root){
        Queue<TreeNode> stack = new LinkedList<>();

        stack.add(root);
        int depth = 0;
        while(!stack.isEmpty()){

            int len = stack.size();
            for (int i = 0 ; i < len ; i++ ){
                TreeNode node = stack.poll();
                if (node.getLeft() != null){
                    stack.add(node.getLeft());
                }
                if (node.getRight() != null){
                    stack.add(node.getRight());
                }
            }
            depth++ ;

        }

        return depth;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.setVal(1);
        TreeNode left = new TreeNode(2) ;
        root.setLeft(left);
        TreeNode right = new TreeNode(3);
        root.setRight(right);

        TreeNode tmp = left;
        left = new TreeNode(4) ;
        tmp.setLeft(left);

        tmp = right;
        right = new TreeNode(5) ;
        tmp.setRight(right);

        System.out.println(solution(root));
        System.out.println(solution2(root));
    }
}
