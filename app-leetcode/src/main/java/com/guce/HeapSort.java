package com.guce;

/**
 * Created by chengen.gu on 2019/10/10.
 */
public class HeapSort {

    /**
     * 建堆   小顶堆
     * @param arr
     * @param length
     * @param idx
     */
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

    /**
     * 大顶堆
     *
     * @param arr
     * @param idx
     * @param length
     */
    public static void maxHeapAdjust(int arr[], int length, int idx) {
        int tmp = arr[idx];
        for (int i = 2 * idx + 1; i < length; i = 2 * i + 1) {
            if (i + 1 < length) {
                if (arr[i] < arr[i + 1]) {
                    i++;
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
            maxHeapAdjust(arr, len, i);
        }

//        swap(arr,0,arr.length -1);

        print(arr);
        for (int i = arr.length - 1; i >= 1; i--) {
            swap(arr, 0, i);
            System.out.println("");
            print(arr);
            maxHeapAdjust(arr, i, 0);
        }
        System.out.println("");
        print(arr);
        System.out.println("==================");
        arr = new int[]{1, 4, 7, 2, 5, 3, 9, 6, 8};
        HeapSort heap = new HeapSort();
        heap.heapSort(arr);
        print(arr);

    }

    public void heapSort(int[] arr) {
        int heapSize = arr.length;

        buildMaxHeap(arr, heapSize);
        for (int i = heapSize - 1; i >= 0; i--) {
            swap(arr, 0, i);
            maxHeapify(arr, 0, i);
        }
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


    public int findKthLargest(int[] nums, int k) {
        int heapSize = nums.length;
        buildMaxHeap(nums, heapSize);
        for (int i = nums.length - 1; i >= nums.length - k + 1; --i) {
            swap(nums, 0, i);
            --heapSize;
            maxHeapify(nums, 0, heapSize);
        }
        return nums[0];
    }

    public void buildMaxHeap(int[] a, int heapSize) {
        for (int i = heapSize / 2; i >= 0; --i) {
            maxHeapify(a, i, heapSize);
        }
    }

    public void maxHeapify(int[] a, int i, int heapSize) {
        int l = i * 2 + 1, r = i * 2 + 2, largest = i;
        if (l < heapSize && a[l] > a[largest]) {
            largest = l;
        }
        if (r < heapSize && a[r] > a[largest]) {
            largest = r;
        }
        if (largest != i) {
            swap(a, i, largest);
            maxHeapify(a, largest, heapSize);
        }
    }


}
