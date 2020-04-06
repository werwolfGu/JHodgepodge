package com.guce;

import java.util.Stack;

/**
 * @Author chengen.gce
 * @DATE 2020/4/1 11:04 下午
 * 括号匹配
 */
public class BracketMatch {

    /**
     * 使用堆栈实现
     * @param str
     * @return
     */
    public static boolean solution(String str){
        Stack<Character> stock = new Stack<>();

        for ( int i = 0 ; i < str.length() ;i++ ){
            Character ch = str.charAt(i);
            if (ch.equals('(')){
                stock.push(ch);
                continue;
            }
            if ( ch.equals(')')){
                if (stock.isEmpty()){
                    return false;
                }
                stock.pop();
            }

        }
        return stock.isEmpty();
    }

    public static boolean solution2(String str){
        int dept = 0;
        for (int i = 0 ; i < str.length() ; i++ ){
            Character ch = str.charAt(i);
            if (ch.equals('(')){
                dept++;
                continue;
            }
            if ( ch.equals(')')){
                if (dept <= 0){
                    return false;
                }
                dept--;

            }
        }
        return dept == 0 ;
    }

    public static void main(String[] args) {
        System.out.println(solution("()()((()))"));
        System.out.println(solution2("()()((()))"));
        System.out.println(solution2("()("));
    }
}
