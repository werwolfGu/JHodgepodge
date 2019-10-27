package com.guce;

import java.util.Stack;

/**
 * 括号匹配
 * Created by chengen.gu on 2018/9/26.
 * https://leetcode-cn.com/problems/valid-parentheses/
 */
public class ValidBrackets {

    public static boolean solution(String s) {
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < s.length(); i++) {
            Character ch = s.charAt(i);
            if (ch == '(' || ch == '{' || ch == '[') {
                stack.push(ch);
                continue;
            }

            if (ch == ')' || ch == ']' || ch == '}') {

                if (stack.size() <= 0) {
                    return false;
                }
                Character tmp = stack.pop();
                if (ch == ')' && tmp == '(') {
                    continue;
                }
                if (ch == ']' && tmp == '[') {
                    continue;
                }
                if (ch == '}' && tmp == '{') {
                    continue;
                }

                return false;
            }

        }

        if (stack.size() > 0) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(solution("()[{]"));
    }

}
