package com.guce;

/**
 * @Author chengen.gu
 * @DATE 2019/10/29 9:38 下午
 * https://leetcode-cn.com/problems/implement-strstr/
 */
public class StrStr {

    public static int solution(String hayStack, String needle) {

        int idx = 0;
        if (hayStack == null || needle == null){
            return idx;
        }


        while (idx < hayStack.length()) {
            int i = idx;
            if ((hayStack.length() - i) < needle.length()) {
                return -1;
            }
            boolean flag = true;
            for (int j = 0; j < needle.length(); j++) {
                if (hayStack.charAt(i++) == needle.charAt(j)) {
                    continue;
                }
                flag = false;
            }

            if (flag) {
                return idx;
            }
            idx++;
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(StrStr.solution("he22222llo", "ll"));
    }
}
