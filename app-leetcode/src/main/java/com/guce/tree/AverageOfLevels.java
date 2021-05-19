package com.guce.tree;

import com.guce.module.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2020/9/12 10:35 上午
 * https://leetcode-cn.com/problems/average-of-levels-in-binary-tree/
 *
 * 给定一个非空二叉树, 返回一个由每层节点平均值组成的数组。
 */
public class AverageOfLevels {

    /**
     * 广度优先遍历
     * @param root
     * @return
     */
    public static List<Double> averageOfLevels(TreeNode root){

        Deque<TreeNode> deque = new ArrayDeque<>();
        deque.add(root);
        List<Double> results = new ArrayList<>();
        while(!deque.isEmpty()){
            int level = deque.size();
            Double sum = 0D;
            for (int i = 0 ; i < level ; i++ ){
                TreeNode node = deque.poll();
                if (node.getLeft() != null) {
                    deque.add(node.getLeft());
                }
                if (node.getRight() != null){
                    deque.add(node.getRight());
                }
                sum += node.getVal();
            }
            results.add(sum / level);
        }

        return results;
    }

    public static void main(String[] args) {
        TreeNode node =TreeNode. createBinaryTree(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, 0);

        System.out.println(averageOfLevels(node));
    }
}
