package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/6/15 11:09 下午
 * https://leetcode-cn.com/problems/add-binary/
 *
 * 给你两个二进制字符串，返回它们的和（用二进制表示）。
 *
 * 输入为 非空 字符串且只包含数字 1 和 0。
 *
 *  
 *
 * 示例 1:
 *
 * 输入: a = "11", b = "1"
 * 输出: "100"
 */
public class AddBinary {

    public static String addBinary(String a, String b){

        StringBuffer result = new StringBuffer();
        int aLen = a.length() - 1;
        int bLen = b.length() - 1;
        int z = 0;
        while( aLen >= 0 || bLen >= 0){
            if (aLen >= 0 && bLen >= 0){
                int x = a.charAt(aLen--) - '0';
                int y = b.charAt(bLen--) - '0';
                int sum = x + y + z;
                z = 0 ;
                if (sum >= 2){
                    z = 1;
                }
                sum = sum % 2;
                result.insert(0,sum);
                continue;
            }
            if (aLen >= 0){
                int x = a.charAt(aLen--) - '0';
                int sum = x + z;
                z = 0 ;
                if (sum >= 2){
                    z = 1;
                }
                sum = sum % 2;
                result.insert(0,sum);
                continue;
            }
            if (bLen >= 0){
                int x = a.charAt(bLen--) - '0';
                int sum = x + z;
                z = 0 ;
                if (sum >= 2){
                    z = 1;
                }
                sum = sum % 2;
                result.insert(0,sum);
                continue;
            }
        }
        if (z != 0){
            result.insert(0,z);
        }
        return result.toString();
    }

    public static String addBinary1(String a, String b) {

        StringBuffer result = new StringBuffer();
        int alen = a.length() - 1 ,blen = b.length() - 1;
        int multi = 0;
        while (alen >= 0 || blen >= 0){
            int x = 0,y=0;
            if (alen >= 0 ){
                x =  a.charAt(alen--) - '0';
            }
            if (blen >= 0){
                y =  b.charAt(blen--) - '0';
            }
            int z = x + y + multi;
            if ( z > 0){
                multi = z / 2;
                z = z % 2 ;
            }
            result.insert(0,z);
        }

        if ( multi > 0){
            result.insert(0,multi);
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(addBinary("1011","1"));
        System.out.println(addBinary1("1011","1"));
        System.out.println(Math.sqrt(4));
    }
}
