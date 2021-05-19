package com.guce;

import com.guce.module.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @Author chengen.gce
 * @DATE 2020/5/31 11:16 下午
 * https://leetcode-cn.com/problems/symmetric-tree/
 *
 *
 * 给定一个二叉树，检查它是否是镜像对称的。
 *
 *  
 *
 * 例如，二叉树 [1,2,2,3,4,4,3] 是对称的。
 *
 *     1
 *    / \
 *   2   2
 *  / \ / \
 * 3  4 4  3
 *  
 *
 * 但是下面这个 [1,2,2,null,3,null,3] 则不是镜像对称的:
 *
 *     1
 *    / \
 *   2   2
 *    \   \
 *    3    3
 *
 */
public class IsSymmetric {

    public static boolean solution(TreeNode root) {
        return check(root, root);
    }

    /**
     * 递归实现
     * @param left
     * @param right
     * @return
     */
    public static boolean check(TreeNode left, TreeNode right) {

        if (left == null && right == null) {
            return true;
        }

        if (left == null || right == null) {
            return false;
        }

        return left.getVal() == right.getVal()
                && check(left.getLeft(), right.getRight()) && check(left.getRight(), right.getLeft());
    }

    public static boolean solution2(TreeNode root) {
        return check2(root, root);
    }

    /**
     * 迭代实现
     * @param l
     * @param r
     * @return
     */
    public static boolean check2(TreeNode l, TreeNode r) {

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(l);
        queue.add(r);
        while (!queue.isEmpty()){
           l = queue.poll();
           r = queue.poll();
           if (l == null && r == null){
               continue;
           }

           if (l == null || r == null || l.getVal() != r.getVal()){
               return false;
           }

           queue.add(l.getLeft());
           queue.add(r.getRight());

           queue.add(l.getRight());
           queue.add(r.getLeft());

        }
        return true;
    }

    public static void main(String[] args) {

        TreeNode root = TreeNode.createBinaryTree(new Integer[]{1, 2, 2, 3, 4, 4, 3});

        System.out.println(solution(root));
        System.out.println(solution2(root));
    }
}
