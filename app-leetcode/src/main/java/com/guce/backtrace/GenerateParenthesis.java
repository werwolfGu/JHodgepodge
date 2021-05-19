package com.guce;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by chengen.gu on 2019/10/25.
 * 给定一个整数n代表生成括号的对数 ，求生成括号的可能情况
 * 比如 3
 * 输出：
 * [((())), (()()), (())(), ()(()), ()()()]
 * https://leetcode-cn.com/problems/generate-parentheses/
 */
public class GenerateParenthesis {


    public List<String> solution(int n) {

        List<String> list = new ArrayList(2 * n);

        backtrace("", list, 2 * n, 0);
        return list;
    }

    /**
     * @param s    回溯每一次的字符串
     * @param list 结果集
     * @param n    最终字符串的长度
     * @param num  期望括号的数目
     */
    public void backtrace(String s, List<String> list, int n, int num) {

        if (s.length() == n && num == 0) {
            list.add(s);
            return;
        }
        if (num < 0 || (s.length() == n && num != 0)) {
            return;
        }
        int v1 = num + 1;
        int v2 = num - 1;
        backtrace(s + "(", list, n, v1);
        backtrace(s + ")", list, n, v2);
    }

    /**
     * @param sb
     * @param left
     * @param right
     * @param n
     * @param list
     */
    public void dfs(StringBuilder sb, int left, int right, int n, List<String> list) {
        if (left == n && right == n) {
            list.add(sb.toString());
            return;
        }
        if (left < right) {
            return;
        }

        if (left < n) {
            dfs(sb.append("("), left + 1, right, n, list);
            sb.deleteCharAt(sb.length() - 1);
        }
        if (right < n) {
            dfs(sb.append(")"), left, right + 1, n, list);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    public List<String> solution2(int n) {
        List<String> result = new ArrayList<>();
        dfs(new StringBuilder(), 0, 0, n, result);
        return result;
    }

    public static boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        int idx = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if ('(' == ch || '[' == ch || '{' == ch) {
                stack.push(ch);
            } else {
                if (!stack.isEmpty()) {
                    char popChar = stack.pop();
                    if ('(' == popChar && ')' == ch) {
                        continue;
                    }
                    if ('[' == popChar && ']' == ch) {
                        continue;
                    }
                    if ('{' == popChar && '}' == ch) {
                        continue;
                    }
                }
                return false;
            }
        }
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        GenerateParenthesis generateParenthesis = new GenerateParenthesis();
        List<String> list = generateParenthesis.solution(3);
        System.out.println(list);
        list = generateParenthesis.solution2(3);
        System.out.println(list);
    }

}
