package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/4/1 11:13 下午
 * https://leetcode-cn.com/problems/maximum-nesting-depth-of-two-valid-parentheses-strings/
 * 有效括号的嵌套深度
 */
public class MaxDepthAfterSplit {

    public static int[] solution(String seq){
        int[] dept = new int[seq.length()];
        int idx = 0 ;
        for (int i = 0 ; i < seq.length() ; i++ ){
            Character ch = seq.charAt(i);
            if (ch.equals('(')){
                dept[i] = idx++ % 2;
            }else {
                dept[i] = --idx % 2;
            }
        }
        return dept;
    }

    public static void main(String[] args) {
        int[] dpt = solution("()(())()");
        for (int i : dpt){
            System.out.print(i + " ");
        }

    }
}
