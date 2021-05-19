package com.guce;

import com.guce.module.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @Author chengen.gce
 * @DATE 2020/5/13 8:45 下午
 *
 * https://leetcode-cn.com/problems/binary-tree-level-order-traversal/
 * 二叉树的层序遍历
 */
public class LevelOrder {

    public static List<List<Integer>> solution(TreeNode root){

        List<List<Integer>> result = new ArrayList<>();
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        while ( !queue.isEmpty()){
            int queueSize = queue.size();
            List<Integer> list = new ArrayList<>();
            while(queueSize > 0){

                TreeNode node = queue.poll();
                list.add(node.getVal());
                if (node.getLeft() != null){
                    queue.add(node.getLeft());
                }
                if (node.getRight() != null){
                    queue.add(node.getRight());
                }
                queueSize--;
            }
            result.add(list);
        }

        return result;

    }
}
