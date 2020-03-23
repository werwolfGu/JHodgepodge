package com.guce;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author chengen.gce
 * @DATE 2020/3/22 7:40 下午
 * https://leetcode-cn.com/problems/minimum-increment-to-make-array-unique/
 * 给定整数数组 A，每次 move 操作将会选择任意 A[i]，并将其递增 1。
 * <p>
 * 返回使 A 中的每个值都是唯一的最少操作次数。
 */
public class MinIncrementForUnique {
    /**
     * 解法 ： 1 ；
     *
     * @param A
     * @return
     */
    public static int solution(int[] A) {
        Set<Integer> set = new HashSet<>();
        int res = 0;

        for (int i = 0; i < A.length; i++) {
            while (set.contains(A[i])) {
                A[i] += 1;
                res++;
            }
            set.add(A[i]);

        }
        return res;
    }

    public static int solution2(int[] A) {

        int[] countArr = new int[8000];
        for (Integer x : A) {
            countArr[x]++;
        }
        int token = 0, ans = 0;
        for (int i = 0; i < 8000; i++) {
            if (countArr[i] >= 2) {
                token += countArr[i] - 1;
                //这里减去 i的值 是方便后面加 i值
                ans -= i * (countArr[i] - 1);
            } else if (token > 0 && countArr[i] == 0) {
                //因为前面减去了i的值  这里就可以直接加上 i 的值了
                token--;
                ans += i;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        System.out.println(solution(new int[]{3, 2, 1, 2, 1, 7}));
    }
}
