package com.guce;

/**
 * Created by chengen.gu on 2019/10/10.
 */
public class HeapSort {

    private static void headAdjust(int[] list, int len, int i) {
        int k = i, temp = list[i], index = 2 * k + 1;
        while (index < len) {
            if (index + 1 < len) {
                if (list[index] < list[index + 1]) {
                    index = index + 1;
                }
            }
            if (list[index] > temp) {
                list[k] = list[index];
                k = index;
                index = 2 * k + 1;
            } else {
                break;
            }
        }
        list[k] = temp;
    }


    /**
     * 建堆
     * @param arr
     * @param length
     * @param idx
     */
    public static void heapAdjust(int arr[], int length, int idx) {

        int tmp = arr[idx];
        for (int i = 2 * idx + 1; i < length; i = 2 * i + 1) {
            if (i + 1 < length) {
                if (arr[i] < arr[i + 1]) {
                    i = i + 1;
                }
            }
            if (arr[i] > tmp) {
                arr[idx] = arr[i];
                idx = i;

            }
        }
        arr[idx] = tmp;
    }

    public static void main(String[] args) {
        int arr[] = {1, 4, 7, 2, 5, 3, 9, 6, 8};

        int idx = arr.length / 2;
        int len = arr.length;
        //建最大堆
        for (int i = idx; i >= 0; i--) {
            heapAdjust(arr, len, i);
        }

//        swap(arr,0,arr.length -1);

        print(arr);
        for (int i = arr.length - 1; i >= 1; i--) {
            swap(arr, 0, i);
            System.out.println("");
            ;
            print(arr);
            heapAdjust(arr, i, 0);
        }
        System.out.println("");
        ;
        print(arr);

    }

    public static void swap(int arr[], int src, int dest) {
        int tmp = arr[src];
        arr[src] = arr[dest];
        arr[dest] = tmp;
    }

    public static void print(int arr[]) {
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }
}
