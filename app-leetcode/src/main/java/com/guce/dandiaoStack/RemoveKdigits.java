package com.guce.dandiaoStack;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @Author chengen.gce
 * @DATE 2021/3/25 8:57 下午
 */
public class RemoveKdigits {

    public String removeKdigits(String num, int k) {


        Deque<Character> stack = new ArrayDeque<>();

        for (int i = 0 ; i < num.length() ; i++ ){
            Character digit = num.charAt(i);
            while (!stack.isEmpty() && k > 0 && stack.peekLast() > digit){
                stack.pollLast();
                k--;
            }
            stack.addLast(digit);
        }
        for (int i = 0 ; i < k ;i++ ){
            stack.pollLast();
        }
        StringBuffer sb = new StringBuffer();
        boolean leadingZero = true;
        while (!stack.isEmpty()){
            Character digit = stack.pollFirst();
            if (leadingZero && digit == '0'){
                continue;
            }
            leadingZero = false;
            sb.append(digit);
        }
        return sb.toString();
    }
}
