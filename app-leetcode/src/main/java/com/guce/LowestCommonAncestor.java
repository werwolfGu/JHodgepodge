package com.guce;

import com.guce.module.TreeNode;

/**
 * @Author chengen.gce
 * @DATE 2020/5/10 5:02 下午
 * https://leetcode-cn.com/problems/lowest-common-ancestor-of-a-binary-tree/submissions/
 * 给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。
 */
public class LowestCommonAncestor {

    public TreeNode ans = null;

    public TreeNode solution(TreeNode root ,TreeNode p ,TreeNode q){
        this.dfs(root,p,q);
        return ans;
    }

    public boolean dfs(TreeNode root ,TreeNode p ,TreeNode q){
        if (root == null){
            return false;
        }

        boolean l = dfs(root.getLeft(),p ,q);
        boolean r = dfs(root.getRight(),p,q);
        if ( (l && r) || ((root.getVal() == p.getVal() || root.getVal() == q.getVal()) && (l || r) )){
            ans = root;
        }
        return l || r || (root.getVal() == p.getVal() || root.getVal() == q.getVal());
    }
}
