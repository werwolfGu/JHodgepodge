package com.guce.common;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import com.google.common.collect.Multimap;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * @Author chengen.gce
 * @DATE 2023/2/27 21:11
 */
public class MapToFlattenMap {

    @Deprecated
    public static void flatMap(Map<String,?> srcMap , Multimap<String,Object> destMap, Deque<String> stack) {

        for (Map.Entry<String, ?> entry : srcMap.entrySet()) {
            Object value = entry.getValue();
            String key = entry.getKey();
            Class<?> clazz = value.getClass();
            if (ClassUtil.isSimpleValueType(value.getClass())) {
                key = queueToStr(stack) + key;
                destMap.put(key,value);
                continue;
            }
            if (Collection.class.isAssignableFrom(clazz)) {
                List<Map<String,?>> list = (List<Map<String, ?>>) value;
                for (int i = 0 ; i < list.size() ; i++) {
                    Map<String,?> map = list.get(i);
                    stack.addLast(key);
                    flatMap(map,destMap,stack);
                    stack.removeLast();
                }
            }
        }
    }

    public void toFlattenMap(Object obj,Multimap<String,Object> destMap ,Deque<String> path) {

        Class<?> clazz = obj.getClass();
        if (ClassUtil.isSimpleValueType(clazz)) {
            String keyPath = queueToStr(path);
            if (keyPath == null) {
                keyPath = clazz.getName();
            }
            destMap.put(keyPath,obj);
            return ;
        }

        if (Map.class.isAssignableFrom(clazz)) {
            Map<String,Object> objMap = (Map<String, Object>) obj;
            objMap.entrySet().stream().forEach( entry -> {
                if (entry.getValue() == null) {
                    return ;
                }
                path.addLast(entry.getKey());
                toFlattenMap(entry.getValue(),destMap,path);
                path.removeLast();
            });
            return ;
        }

        if (Collection.class.isAssignableFrom(clazz)) {
            Collection<?> collection = (Collection<?>) obj;
            collection.stream().forEach( val -> {
                if (val == null) {
                    return ;
                }
                toFlattenMap(val,destMap,path);
            });
            return ;
        }

        if (clazz.isArray()) {
            int len = Array.getLength(obj);
            for (int i = 0 ; i < len ; i++ ) {
                Object val = Array.get(obj,i);
                toFlattenMap(val,destMap,path);
            }
            return ;
        }

        Map<String,Object> val = BeanUtil.beanToMap(obj);
        toFlattenMap(val,destMap,path);

    }

    private static String queueToStr(Deque<String> deque) {
        if (deque.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String element : deque) {
            sb.append(element).append(".");
        }
        return sb.substring(0,sb.length() - 1);
    }
    //快速排序
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int middle = getMiddle(arr, low, high);
            quickSort(arr, low, middle - 1);
            quickSort(arr, middle + 1, high);
        }
    }
    //getMiddle
    public static int getMiddle(int[] arr, int low, int high) {
        int temp = arr[low];
        while (low < high) {
            while (low < high && arr[high] >= temp) {
                high--;
            }
            arr[low] = arr[high];
            while (low < high && arr[low] <= temp) {
                low++;
            }
            arr[high] = arr[low];
        }
        arr[low] = temp;
        return low;
    }

    //堆排序
    public static void heapSort(int[] arr) {
        for (int i = arr.length / 2 - 1; i >= 0; i--) {
            adjustHeap(arr, i, arr.length);
        }
        for (int i = arr.length - 1; i > 0; i--) {
            int temp = arr[i];
            arr[i] = arr[0];
            arr[0] = temp;
            adjustHeap(arr, 0, i);
        }
    }
    //adjustHeap
    public static void adjustHeap(int[] arr, int i, int length) {
        int temp = arr[i];
        for (int k = i * 2 + 1; k < length; k = k * 2 + 1) {
            if (k + 1 < length && arr[k] < arr[k + 1]) {
                k++;
            }
            if (arr[k] > temp) {
                arr[i] = arr[k];
                i = k;
            } else {
                break;
            }
        }
        arr[i] = temp;
    }
    //List套List转List
    public static List<Integer> listToList(List<List<Integer>> list) {
        List<Integer> result = new ArrayList<>();
        for (List<Integer> l : list) {
            result.addAll(l);
        }
        return result;
    }
    //List多层嵌套转List
    public static List<Integer> listToFlatList(List<?> list) {
        List<Integer> result = new ArrayList<>();
        for (Object o : list) {
            if (o instanceof List) {
                result.addAll(listToFlatList((List<?>) o));
            } else {
                result.add((Integer) o);
            }
        }
        return result;
    }

    public static void main(String[] args) {

        int[] arr = {1, 3, 2, 6, 5, 7, 8, 9, 4};
        quickSort(arr, 0, arr.length - 1);
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }
}
