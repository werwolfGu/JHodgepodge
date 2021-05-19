package com.guce.tree;

import com.guce.module.TreeNode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2020/9/26 5:34 下午
 */
public class pathSum2 {

    public List<List<Integer>> pathSum(TreeNode root, int sum) {

        List<List<Integer>> result = new ArrayList<>();

        Deque<Integer> deque = new ArrayDeque<>();
        dfs(root,0,sum,deque,result);
        return result;
    }

    public void dfs(TreeNode node, int sum, int target, Deque<Integer> list, List<List<Integer>> result ){

        if (node == null){
            return ;
        }
        list.add(node.val);
        sum += node.val;
        if (node.left == null && node.right == null && target == sum){
            result.add(new ArrayList<>(list));
        }

        dfs(node.left,sum ,target,list,result);
        dfs(node.right,sum ,target,list,result);
        list.removeLast();
    }


    List<List<Integer>> ret = new LinkedList<List<Integer>>();
    Deque<Integer> path = new LinkedList<Integer>();

    public List<List<Integer>> pathSum1(TreeNode root, int sum) {
        dfs(root, sum);
        return ret;
    }

    public void dfs(TreeNode root, int sum) {
        if (root == null) {
            return;
        }
        path.offerLast(root.val);
        sum -= root.val;
        if (root.left == null && root.right == null && sum == 0) {
            ret.add(new LinkedList<Integer>(path));
        }
        dfs(root.left, sum);
        dfs(root.right, sum);
        path.pollLast();
    }


    public static void main(String[] args) {
        pathSum2 p = new pathSum2();
        TreeNode node = TreeNode.createBinaryTree(new Integer[]{5,4,8,11,null,13,4,7,2,null,null,5,1},0);
        System.out.println(p.pathSum(node,22));
        System.out.println(p.pathSum1(node,22));
    }
}
