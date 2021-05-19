package com.guce.tree;

import com.guce.module.TreeNode;

/**
 * @Author chengen.gce
 * @DATE 2021/3/17 10:24 下午
 * https://leetcode-cn.com/problems/er-cha-sou-suo-shu-yu-shuang-xiang-lian-biao-lcof/
 */
public class TreeToDoublyList {

    TreeNode pre = null, head = null;
    public TreeNode solution(TreeNode root){
        if (root == null){
            return null;
        }
        dfs(root);
        head.left = pre;
        pre.right = head;
        return head;
    }

    public void dfs(TreeNode curr){
        if (curr == null){
            return ;
        }
        dfs (curr.left);
        if (pre != null) {
            pre.right = curr;
        }else {
            head = curr;
        }
        curr.left = pre;
        pre = curr;
        dfs(curr.right);
    }
}
