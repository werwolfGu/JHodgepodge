package com.guce;

/**
 * @Author chengen.gu
 * @DATE 2020/2/16 8:46 下午
 *https://leetcode-cn.com/problems/longest-common-prefix/solution/zui-chang-gong-gong-qian-zhui-by-leetcode/
 * 最长公共前缀
 */
public class LongestCommonPrefix {

    public static String solution(String[] strs){

        //找到最短的str
        int minLen = strs[0].length();

        for (int i = 1 ; i < strs.length ; i++ ){
            minLen = Math.min(minLen,strs[i].length());
        }
        //二分查找法
        int low = 1, high = minLen;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (patch(strs, mid)) {
                low = mid + 1;
            }else {
                high = mid - 1;
            }
        }
        if ( high == 0 ){
            return "";
        }
        return strs[0].substring(0, (low + high) / 2);

    }

    public static boolean patch(String[] strs, int len) {

        String str = strs[0].substring(0, len);
        for (int i = 1; i < strs.length; i++) {
            if (!strs[i].startsWith(str)) {
                return false;
            }
        }
        return true;
    }


    public static String solution2(String[] strs) {
        if (strs.length == 0) return "";
        String prefix = strs[0];
        for (int i = 1; i < strs.length; i++)
            while (strs[i].indexOf(prefix) != 0) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty()) return "";
            }
        return prefix;
    }
    public static void main(String[] args) {

        System.out.println(LongestCommonPrefix.solution(new String[]{"dog","racecar","car"}));

        System.out.println(LongestCommonPrefix.solution2(new String[]{"flower", "flow", "flight"}));


        System.out.println("123445".indexOf("6"));
    }

}
