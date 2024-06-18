package com.guce.排列组合;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2024/6/9 13:15
 */
public class 字符串全排列 {

    public List<String> permutation(String[] strArr) {
        boolean[] used = new boolean[strArr.length];
        Deque<String> stack = new ArrayDeque<>();

        int idx = 0 ;
        List<String> result = new ArrayList<>();

        dfs(strArr,idx,used,stack,result);
        return result;


    }

    public void dfs(String[] strArr , int idx , boolean[] used, Deque<String> stack , List<String> result) {

        if (strArr.length == idx) {
            result.add(takeStack(stack));
            return ;
        }
        for (int i = 0 ; i < strArr.length ; i++) {
            if (used[i]) {
                continue;
            }
            stack.addLast(strArr[i]);
            used[i] = true;
            idx++;
            dfs(strArr , idx , used , stack , result);
            stack.removeLast();
            used[i] = false;
            idx--;
        }
    }

    public String takeStack(Deque<String> stack){
        StringBuilder sb = new StringBuilder();

        for (String str : stack) {
            sb.append(str);
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        字符串全排列 solution = new 字符串全排列();

        String[] strArr = new String[]{"a","b","c"};
        System.out.println(solution.permutation(strArr));

        String ab = "abcdefghigklmnopqrstuvwxyz" ;
        System.out.println(ab.indexOf("df"));
    }
}
