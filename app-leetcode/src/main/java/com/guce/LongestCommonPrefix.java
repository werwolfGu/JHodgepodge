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
        int l = 0 ,minLen = strs[0].length();

        for (int i = 1 ; i < strs.length ; i++ ){
            minLen = Math.min(minLen,strs[i].length());
        }

        //二分查找法

        int low = l , high = minLen;
        while (low < high){
            int mid = (low + high) / 2;
            if (patch(strs,low,mid)){
                low = mid ;
            }else {
                high = mid - 1;
            }
        }
        if ( high == 0 ){
            return "";
        }
        return strs[0].substring(0,high);

    }

    public static boolean patch(String[] strs ,int low,int hight){

        String str = strs[0].substring(low,hight);
        for ( int i = 0 ; i < strs.length ; i++ ){
            String subString = strs[i].substring(low,hight);
            if ( !str.equals(subString)){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        System.out.println(LongestCommonPrefix.solution(new String[]{"dog","racecar","car"}));
    }

}
