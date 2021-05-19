package com.guce;

import com.guce.module.TreeNode;

/**
 * @Author chengen.gce
 * @DATE 2020/5/13 8:38 下午
 * https://leetcode-cn.com/problems/lowest-common-ancestor-of-a-binary-search-tree/solution/er-cha-sou-suo-shu-de-zui-jin-gong-gong-zu-xian--2/
 *
 */
public class LowestCommonAncestor1 {

    public static TreeNode solution(TreeNode root ,TreeNode p , TreeNode q){

        int parentValue = root.getVal();
        int pVal = p.getVal();
        int qVal = q.getVal();
        if (pVal > parentValue && qVal > parentValue){
            return solution(root.getRight(),p,q);
        }
        if (pVal < parentValue && qVal < parentValue){
            return solution(root.getLeft(),p,q);
        }
        return root;
    }

}
