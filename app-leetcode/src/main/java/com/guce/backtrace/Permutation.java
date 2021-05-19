package com.guce.backtrace;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author chengen.gce
 * @DATE 2020/9/10 9:13 下午
 * https://leetcode-cn.com/problems/permutations/
 * 给定一个 没有重复 数字的序列，返回其所有可能的全排列。
 */
public class Permutation {

    public String[] permutation(String S) {
        List<String> list = new ArrayList<>();
        boolean[] used = new boolean[S.length()];
        Deque<Character> deque = new ArrayDeque<>();
        backtrack(S,deque,list,used);
        return list.toArray(new String[0]);
    }

    public void backtrack(String S,Deque<Character> deque,List<String> result,boolean[] used){

        if (deque.size() == S.length()){
            StringBuilder sb = new StringBuilder();
            for(Character ch : deque){
                sb.append(ch);
            }
            result.add(sb.toString());
            return ;
        }

        for (int i = 0 ; i < S.length() ; i++ ){
            if (used[i]){
                continue;
            }

            deque.add(S.charAt(i));
            used[i] = true;
            backtrack(S,deque,result,used);
            used[i] = false;
            deque.removeLast();
        }
    }

    public String[] permutation1(String S) {
        char[] arr = S.toCharArray() ;
        Set<String> list = new HashSet<>();
        boolean[] used = new boolean[S.length()];
        backtrack(arr,used , new StringBuffer(),list);
        return list.toArray(new String[0]);
    }

    public static void backtrack(char[] arr , boolean[] used ,StringBuffer S , Set<String> list){
        int len = S.length();
        if (len == arr.length ){
            list.add(S.toString());
            return ;
        }

        for (int i = 0 ; i < arr.length ; i++ ){
            if (used[i]){
                continue;
            }
            char ch = arr[i];
            S.append(ch);
            used[i] = true;
            backtrack(arr,used,S,list);
            S.deleteCharAt(S.length() -1);
            used[i] = false;
        }
    }


    public static void main(String[] args) {

        Permutation permutation = new Permutation();
        String[]  strArr = permutation.permutation("123");

        for (String str : strArr){
            System.out.println(str);
        }

        strArr = permutation.permutation1("qqe");
        for (String str : strArr){
            System.out.println(str);
        }
    }

}
