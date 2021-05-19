package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/11/22 4:54 下午
 *
 * https://leetcode-cn.com/problems/check-if-two-string-arrays-are-equivalent/
 */
public class ArrayStringsAreEqual {


    public static boolean arrayStringsAreEqual(String[] word1, String[] word2) {

        int[] table = new int[1000];
        int idx = 0 ;
        for (int i = 0 ; i < word1.length ; i++){

            for (int j = 0 ; j < word1[i].length() ; j++ ){
                table[idx++ ] = word1[i].charAt(j) - 'a' ;
            }
        }
        idx = 0 ;
        for (int i = 0 ; i < word2.length ; i++){

            for (int j = 0 ; j < word2[i].length() ; j++ ){
                if ( (table[idx++] - (word2[i].charAt(j) - 'a') ) != 0){
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean arrayStringsAreEqual1(String[] word1, String[] word2) {

        StringBuilder sb1 = new StringBuilder();
        for (String value : word1) {

            sb1.append(value);
        }

        StringBuilder sb2 = new StringBuilder();
        for (String s : word2) {

            sb2.append(s);
        }
        return sb1.toString().equals(sb2.toString());
    }

    public static void main(String[] args) {
        String world1[] =new String[]{"ab", "c"};
        String world2[] =new String[]{"a", "bc"};

        System.out.println(arrayStringsAreEqual(world1,world2));
    }
}
