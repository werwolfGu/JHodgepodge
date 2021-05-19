package com.guce;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2020/4/17 8:39 下午
 * https://leetcode-cn.com/problems/permutation-i-lcci/
 * 排列组合
 */
public class Permutation {

    public static List<String> solution(String str){
        List<String> list = new ArrayList<>();

        list.add(str);
        for (int i = 0 ; i < str.length() -1 ; i++ ){
            int size = list.size();
            for (int j = i + 1 ;j < str.length() ; j++ ){
                for (int idx = 0 ; idx < size ; idx++ ){
                    list.add(swap(list.get(idx),i,j));
                }
            }
        }
        return list;
    }

    public static String swap(String str,int pos1,int pos2){
        char[] chars = str.toCharArray();

        chars[pos1] ^= chars[pos2];
        chars[pos2] ^= chars[pos1];
        chars[pos1] ^= chars[pos2];
        return new String(chars);
    }

    public static void backtrack(String S ,String path, AbstractCollection<String> list){
        if (S == null || S.length() == 0 ){
            list.add(path);
            return ;
        }
        int len = S.length();
        for (int i = 0 ; i < len ; i++ ){
            Character ch = S.charAt(i);
            String tmp = S.substring(0,i)+ S.substring(i+1 ,len);

            backtrack(tmp,path + ch.toString(),list);
        }
    }

    public static AbstractCollection<String> solution1(String S){
        String path = "";
        AbstractCollection<String> list = new ArrayList<>();
        backtrack(S,path,list);
        list.toArray(new String[0]);
        return list;
    }

    /**
     * 有重复的排列组合
     * @param S
     * @return
     */
    public static AbstractCollection<String> solution2(String S){
        String path = "";
        AbstractCollection<String> list = new HashSet<>();
        backtrack(S,path,list);
        list.toArray(new String[0]);
        return list;
    }


    public static void backtrace(String S,List<String> list,String path){
        if (path.length() == S.length()){
            list.add(path);
            return ;
        }

        for (int i = 0 ; i < S.length() ; i++ ){

        }
    }

    public static void main(String[] args) {
        System.out.println(solution("123"));
        System.out.println(solution1("qqe"));
        System.out.println(solution2("qqe"));


    }
}
