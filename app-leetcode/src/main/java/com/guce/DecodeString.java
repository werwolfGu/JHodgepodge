package com.guce;

import java.util.Stack;

/**
 * @Author chengen.gce
 * @DATE 2020/5/28 10:46 下午
 */
public class DecodeString {

    public static String solution(String s){
        String result = new String();

        Stack<Character> stack = new Stack<>();
        String tmpSb = "";
        String nums = "" ;

        for (int i = 0 ; i < s.length() ; i++ ){
            Character ch = s.charAt(i);
            if ('[' == ch){
                stack.push(ch);
                continue;
            }
            if (']' == ch){

                while (!stack.isEmpty()){

                    Character tmp = stack.pop();
                    if ('[' == tmp){
                        break;
                    }
                    tmpSb = tmp + tmpSb;
                }
                while (!stack.isEmpty()){
                    Character tmp = stack.pop();
                    if (tmp >= '0' && tmp <= '9'){
                        nums = tmp + nums;
                        continue;
                    }else {
                        stack.push(tmp);
                        break;
                    }

                }
                String s1 = "";
                int idx = 0;
                if (!"".equals(nums)){
                    idx = Integer.valueOf(nums);
                }
                for (int j = 0 ; j < idx ;j++ ){
                    s1 = tmpSb + s1;
                }
                nums = "";
                tmpSb = s1;
                continue;
            }
            result = result + tmpSb ;
            tmpSb = "";
            stack.push(ch);
        }
        if (!"".equals(tmpSb)){
            result =  result + tmpSb;
        }
        String tmp = "";
        while (!stack.isEmpty()){
            tmp = stack.pop() + tmp;
        }
        result = result + tmp;
        return  result;
    }

    public static void main(String[] args) {

        System.out.println(solution("3[a]2[bc]"));
        System.out.println(solution("3[a2[c]]"));
        System.out.println(solution("2[abc]3[cd]ef"));
        System.out.println(solution("10[a]ef"));
    }
}
