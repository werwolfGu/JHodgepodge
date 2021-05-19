package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2020/6/14 6:36 下午
 * https://leetcode-cn.com/problems/sum-of-mutated-array-closest-to-target/
 * <p>
 * 给你一个整数数组 arr 和一个目标值 target ，请你返回一个整数 value ，使得将数组中所有大于 value 的值变成 value 后，数组的和最接近  target （最接近表示两者之差的绝对值最小）。
 * <p>
 * 如果有多种使得和最接近 target 的方案，请你返回这些整数中的最小值。
 * <p>
 * 请注意，答案不一定是 arr 中的数字。
 * <p>
 *  
 * <p>
 * 示例 1：
 * <p>
 * 输入：arr = [4,9,3], target = 10
 * 输出：3
 * 解释：当选择 value 为 3 时，数组会变成 [3, 3, 3]，和为 9 ，这是最接近 target 的方案。
 * 示例 2：
 * <p>
 * 输入：arr = [2,3,5], target = 10
 * 输出：5
 * 示例 3：
 * <p>
 * 输入：arr = [60864,25176,27249,21296,20204], target = 56803
 * 输出：11361
 */
public class FindBestValue {

    public static int solution(int[] arr, int target) {
        if (arr == null) {
            return 0;
        }
        int value = target / arr.length;
        int sum = 0;
        int curr = Integer.MAX_VALUE;
        while (true) {

            for (int i = 0; i < arr.length; i++) {
                if (arr[i] > value) {
                    sum += value;
                    continue;
                }
                sum += arr[i];

            }
            if (curr <= Math.abs(target - sum)) {
                break;
            }
            if (curr > Math.abs(target - sum)) {
                curr = Math.abs(target - sum);
                /////value 的值不可能小于  target / arr.length 的值，因为如果小于均值的话  只能是求出的值只能是和与target只会比value小
                value++;
                sum = 0;
            }

        }
        return value - 1;

    }

    public static void main(String[] args) {
        System.out.println(solution(new int[]{2, 3, 5}, 10));
    }
}
