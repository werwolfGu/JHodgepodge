package com.guce.backtrace;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2021/3/31 1:38 下午
 * https://leetcode-cn.com/problems/palindrome-partitioning/
 * 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是 回文串 。返回 s 所有可能的分割方案。
 *
 * 回文串 是正着读和反着读都一样的字符串。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：s = "aab"
 * 输出：[["a","a","b"],["aa","b"]]
 * 示例 2：
 *
 * 输入：s = "a"
 * 输出：[["a"]]
 */
public class Partition {

    public List<List<String>> partition(String s) {

        char[] charArr = s.toCharArray();
        int len = charArr.length;
        List<List<String>> res = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();

        dfs(charArr,0,len,stack,res);
        return res;

    }

    public void dfs(char[] charArr , int idx , int len , Deque<String> path, List<List<String>> res){

        if (idx == len){
            res.add(new ArrayList<>(path));
            return ;
        }

        for (int i = idx ; i < len ;i++){

            if ( !isHuiwen(charArr,idx,i)){
                continue;
            }
            path.addLast(new String(charArr,idx , i + 1 - idx));
            dfs(charArr,i + 1, len,path,res);
            path.removeLast();
        }
    }

    public boolean isHuiwen(char[] charArr ,int left ,int right){
        while (left < right){
            if (charArr[left] != charArr[right]){
                return false;
            }
            left++ ;
            right-- ;
        }
        return true;

    }

    public static void main(String[] args) {
        Partition partition = new  Partition();
        System.out.println(partition.partition("aab"));
    }
}
