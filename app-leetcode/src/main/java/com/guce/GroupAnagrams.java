package com.guce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author chengen.gce
 * @DATE 2020/11/22 6:18 下午
 *
 * https://leetcode-cn.com/problems/group-anagrams/
 * 给定一个字符串数组，将字母异位词组合在一起。字母异位词指字母相同，但排列不同的字符串。
 *
 * 示例:
 *
 * 输入: ["eat", "tea", "tan", "ate", "nat", "bat"]
 * 输出:
 * [
 *   ["ate","eat","tea"],
 *   ["nat","tan"],
 *   ["bat"]
 * ]
 *
 */
public class GroupAnagrams {

    ///排序
    public static List<List<String>> groupAnagrams(String[] strs) {

        Map<String,List<String>> map = new HashMap<>();
        for (int i = 0 ; i < strs.length ; i++ ){
            char[] arrStr = strs[i].toCharArray();
            Arrays.sort(arrStr);
            String key = new String(arrStr);
            List<String> list = map.computeIfAbsent(key, k -> new ArrayList<>());
            list.add(strs[i]);
        }

        List<List<String>> result = new ArrayList<>();
        for (Map.Entry<String,List<String>> entry : map.entrySet()) {

            result.add(entry.getValue());
        }
        return result;
    }

    ///还有一种方法 计数法，因为每个字母是小写的26 个字母 ，使用count[26] 记录下字母
    public static List<List<String>> solution2(String[] strs){

        List<List<String>> result = new ArrayList<>();
        Map<String,List<String>> map = new HashMap<>();
        for (int i = 0 ; i < strs.length ; i++){
            int count[] = new int[26];
            String str = strs[i];
            for (int j = 0 ; j < str.length() ;j++){
                count[str.charAt(j) - 'a']++ ;
            }
            StringBuilder sb = new StringBuilder();

            for (int j = 0 ; j < count.length ; j++) {
                if (count[j] != 0){
                    sb.append(j + 'a').append(count[j]);
                }

            }
            List<String> list = map.computeIfAbsent(sb.toString(),k -> new ArrayList<>());
            list.add(str);
        }
        result.addAll(map.values());
        return result;
    }


    public static void main(String[] args) {
        String[] str = new String[]{"eat", "tea", "tan", "ate", "nat", "bat"};

        List<List<String>> result = groupAnagrams(str);
        System.out.println(result);
        System.out.println(solution2(str));
    }
}
