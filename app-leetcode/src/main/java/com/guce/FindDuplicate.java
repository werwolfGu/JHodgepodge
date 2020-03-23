package com.guce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author chengen.gce
 * @DATE 2020/3/16 8:45 下午
 */
public class FindDuplicate {

    public static List<List<String>> solution(String[] paths){

        if ( paths == null || paths.length == 0){
            return null;
        }
        Map<String,List<String>> map = new HashMap<>();

        List<List<String>> result = new ArrayList<>();
        for (int i = 0 ; i < paths.length ; i++ ){
            Map<String,List<String>> tmpMap = filePaser(paths[i]);
            tmpMap.entrySet().forEach( entry -> {
                List<String> list = map.get(entry.getKey());
                if (list == null){
                    map.put(entry.getKey(),entry.getValue());
                }else{
                    list.addAll(entry.getValue());
                }
            });
        }
        map.entrySet().forEach( entry -> {
            if (entry.getValue().size() > 1){
                result.add(entry.getValue());
            }

        });
        return result;
    }

    public static Map<String,List<String>> filePaser(String str){
        if (str == null || str.length() == 0){
            return null;
        }

        String[] pathContent = str.split(" ");

        String path = pathContent[0];
        Map<String,List<String>> map = new HashMap<>();
        for (int i = 1 ; i< pathContent.length ; i++){
            String content = pathContent[i];
            String fileName = content.substring(0,content.indexOf("("));
            String ct = content.substring(content.indexOf("(") + 1,content.length() - 1);
            List<String> list = map.get(ct);
            if (list == null){
                list = new ArrayList<>();
                map.put(ct,list);
            }
            list.add(path + "/" + fileName);

        }
        return map;
    }

    public static void main(String[] args) {
        String[] paths = new String[]{"root/a 1.txt(abcd) 2.txt(efgh)", "root/c 3.txt(abcd)", "root/c/d 4.txt(efgh)", "root 4.txt(efgh)"};

        System.out.println(solution(paths));
        System.out.println(filePaser("root/c/d 4.txt(efgh)"));
    }

}
