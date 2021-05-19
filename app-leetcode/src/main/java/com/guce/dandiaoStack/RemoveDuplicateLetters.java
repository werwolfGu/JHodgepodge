package com.guce.dandiaoStack;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @Author chengen.gce
 * @DATE 2021/3/25 10:37 下午
 */
public class RemoveDuplicateLetters {

    public String removeDuplicateLetters(String s) {

        int[] count = new int[26];
        for (int i = 0 ; i < s.length() ; i++ ){
            count[s.charAt(i) - 'a']++;
        }
        Deque<Character> stack = new ArrayDeque();
        boolean inStack[] = new boolean[26];
        for (int i = 0 ; i < s.length() ;i++ ){
            int idx = s.charAt(i) - 'a';
            count[idx]-- ;
            if (inStack[idx]){
                continue;
            }
            while( !stack.isEmpty() && stack.peekLast() > s.charAt(i)){
                if (count[stack.peekLast() - 'a'] == 0){
                    break;
                }
                inStack[stack.pollLast() - 'a'] = false;
            }
            stack.addLast(s.charAt(i));
            inStack[idx] = true;
        }

        StringBuffer sb = new StringBuffer();
        while(!stack.isEmpty()){
            sb.append(stack.poll());
        }
        return sb.toString();
    }

    public String s1 (String s){
        int[] count = new int[26];
        for (int i = 0 ; i < s.length() ;i++ ){
            count[s.charAt(i) - 'a']++ ;
        }

        boolean[] instack = new boolean[26];
        Deque<Character> stack = new ArrayDeque();

        for (int i = 0 ; i < s.length() ;i++ ){
            int idx = s.charAt(i) - 'a';
            count[idx]--;
            if (instack[idx]){
                continue;
            }

            while( !stack.isEmpty() && stack.peekLast() > s.charAt(i)){
                if (count[stack.peekLast() - 'a'] == 0){
                    break;
                }
                instack[stack.pollLast() - 'a'] = false;
            }
            stack.addLast(s.charAt(i));
            instack[idx] = true;
        }

        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()){
            sb.append(stack.poll());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        RemoveDuplicateLetters removeDuplicateLetters = new RemoveDuplicateLetters();
        System.out.println(removeDuplicateLetters.removeDuplicateLetters("bcabc"));
        System.out.println(removeDuplicateLetters.removeDuplicateLetters("cbacdcbc"));
        System.out.println(removeDuplicateLetters.s1("cbacdcbc"));
    }
}
