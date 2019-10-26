package com.guce;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengen.gu on 2019/10/25.
 * 给定一个整数n代表生成括号的对数 ，求生成括号的可能情况
 * 比如 3
 * 输出：
 * [((())), (()()), (())(), ()(()), ()()()]
 */
public class GenerateParenthesis {


    public List<String> solution(int n ){

        List<String> list = new ArrayList(2*n);

        backtrace("",list,2*n ,0);
         return list;
    }

    /**
     *
     * @param s  回溯每一次的字符串
     * @param list 结果集
     * @param n     最终字符串的长度
     * @param num   期望括号的数目
     */
    public void backtrace(String s, List<String> list, int n, int num ){
        if(s.length() == n && num ==0){
            list.add(s);
            return;
        }
        if(num < 0 || (s.length() == n && num !=0) ){
            return;
        }
        int v1 = num + 1;
        int v2 = num - 1;
        backtrace(s + "(", list, n, v1);
        backtrace(s + ")", list, n, v2);
    }

    public static void main(String[] args) {
        GenerateParenthesis generateParenthesis = new GenerateParenthesis();
        List<String> list = generateParenthesis.solution(3);
        System.out.println(list);
    }

}
