package com.guce;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2024/6/9 13:54
 */
public class FindAnagrams {

    public List<Integer> findAnagrams(String s, String p) {

        List<Integer> result = new ArrayList();
        int pLen = p.length();
        for (int idx  = 0 ;idx < s.length() -  pLen + 1; idx++) {
            int s_p = idx;
            int p_p = 0;
            while(p_p < pLen && s.charAt(s_p) == p.charAt(p_p) ) {
                s_p++;
                p_p++;
            }
            if (p_p == p.length()) {
                result.add(idx);
            }
        }
        return result;
    }

    public static void main(String[] args) {

        FindAnagrams findAnagrams = new FindAnagrams();
        System.out.println(findAnagrams.findAnagrams("cbaebabccd","abc"));
    }
}
