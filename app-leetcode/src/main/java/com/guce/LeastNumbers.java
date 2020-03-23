package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/3/22 8:36 上午
 * https://leetcode-cn.com/problems/zui-xiao-de-kge-shu-lcof/
 */
public class LeastNumbers {

    public static int[] solution(int[] arr, int k) {
        int len = arr.length / 2;

        for (int i = len; i >= 0; i--) {
            heapAdjust(arr, arr.length, i);
        }
        for (int i = arr.length - 1; i >= arr.length - k; i--) {
            swap(arr, 0, i);
            heapAdjust(arr, i, 0);
        }
        int[] res = new int[k];
        int idx = 0;
        for (int i = arr.length - 1; i >= arr.length - k; i--) {
            res[idx++] = arr[i];
        }
        return res;

    }

    public static void swap(int arr[], int left, int right) {
        if (left == right) {
            return;
        }
        arr[left] = arr[left] + arr[right];
        arr[right] = arr[left] - arr[right];
        arr[left] = arr[left] - arr[right];
    }

    public static void heapAdjust(int arr[], int length, int idx) {
        int tmp = arr[idx];
        for (int i = 2 * idx + 1; i < length; i = 2 * i + 1) {
            if (i + 1 < length) {
                if (arr[i] > arr[i + 1]) {
                    i = i + 1;
                }
            }
            if (arr[i] < tmp) {
                arr[idx] = arr[i];
                idx = i;

            }
        }
        arr[idx] = tmp;
    }

    public static void main(String[] args) {

        int[] arr = new int[]{0, 0, 2, 3, 2, 1, 1, 2, 0, 4};
        int k = 10;
        int[] mins = solution(arr, k);
        for (int i = 0; i < k; i++) {
            System.out.println(mins[i]);
        }
    }
}
