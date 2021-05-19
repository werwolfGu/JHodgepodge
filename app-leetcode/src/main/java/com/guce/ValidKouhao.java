package com.guce;

import java.util.Stack;

/**
 * @Author chengen.gce
 * @DATE 2020/5/24 2:02 下午
 * https://leetcode-cn.com/problems/valid-parentheses/
 * 括号是否有效
 */
public class ValidKouhao {

    public static boolean isValid(String s){
        Stack<Character> stack = new Stack<>();
        int idx = 0;
        for (int i = 0 ; i < s.length() ;i++ ){
            char ch = s.charAt(i);
            if ('(' == ch || '[' == ch || '{' == ch) {
                stack.push(ch);
            }else {
                if (!stack.isEmpty()){
                    char popChar = stack.pop();
                    if ('(' == popChar && ')' == ch ){
                        continue;
                    }
                    if ('[' == popChar && ']' == ch ){
                        continue;
                    }
                    if ('{' == popChar && '}' == ch ){
                        continue;
                    }
                }
                return false;
            }
        }
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        System.out.println(isValid("()[]{}"));
    }
}
