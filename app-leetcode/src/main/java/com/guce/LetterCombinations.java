package com.guce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author chengen.gce
 * @DATE 2020/5/27 9:31 下午
 * https://leetcode-cn.com/problems/letter-combinations-of-a-phone-number/
 *
 * 给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。答案可以按 任意顺序 返回。
 *
 * 给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。
 *
 * 回溯法
 */
public class LetterCombinations {

    private static Map<Character,String> map = new HashMap<>();

    static {
        map.put('2',"abc");
        map.put('3',"def");
        map.put('4',"ghi");
        map.put('5',"jkl");
        map.put('6',"mno");
        map.put('7',"pqrs");
        map.put('8',"tuv");
        map.put('9',"wxyz");
    }

    public static List<String> solution(String digits){
        List<String> result = new ArrayList<>();
        backtrace(digits,0,"",result);
        return result;
    }

    public static void backtrace(String digits,int idx,String str ,List<String> result){
        if (idx == digits.length()){
            result.add(str);
            return ;
        }

        Character ch = digits.charAt(idx);
        String letter = map.get(ch);
        for (int i = 0 ; i < letter.length() ; i++ ){
            backtrace(digits,idx + 1, str + letter.charAt(i),result);
        }
    }

    public static void main(String[] args) {
        System.out.println(solution("234"));
    }
}
