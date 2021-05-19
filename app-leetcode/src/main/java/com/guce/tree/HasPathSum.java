package com.guce.tree;

import com.guce.module.TreeNode;

/**
 * @Author chengen.gce
 * @DATE 2020/9/24 10:48 下午
 * https://leetcode-cn.com/problems/path-sum/
 *
 * 给定一个二叉树和一个目标和，判断该树中是否存在根节点到叶子节点的路径，这条路径上所有节点值相加等于目标和。
 *
 * 说明: 叶子节点是指没有子节点的节点。
 *
 * 示例: 
 * 给定如下二叉树，以及目标和 sum = 22，
 *
 *               5
 *              / \
 *             4   8
 *            /   / \
 *           11  13  4
 *          /  \      \
 *         7    2      1
 * 返回 true, 因为存在目标和为 22 的根节点到叶子节点的路径 5->4->11->2。
 */
public class HasPathSum {

    public boolean hasPathSum(TreeNode root, int sum) {
        if (root == null){
            return false;
        }
        return pathSum(root,sum,0);
    }

    public boolean pathSum(TreeNode node ,int target,int sum){

        if (node == null){
            return false;
        }
        if (node.left == null && node.right == null) {
            sum += node.val;
            if (target == sum){
                return true;
            }
            return false;
        }
        sum += node.val;
        boolean leftFlag = pathSum(node.left,target,sum);
        boolean rightFlag = pathSum(node.right,target,sum);

        return leftFlag || rightFlag;
    }

    public static void main(String[] args) {
        TreeNode node = TreeNode.createBinaryTree(new Integer[]{5,4,8,11,null,13,5,7,3,null,null,null,1},0);
        HasPathSum hasPathSum = new HasPathSum();
        System.out.println(hasPathSum.hasPathSum(node,22));
    }
}
