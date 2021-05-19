package com.guce;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @Author chengen.gce
 * @DATE 2020/6/11 9:34 下午
 * https://leetcode-cn.com/problems/daily-temperatures/
 * 请根据每日 气温 列表，重新生成一个列表。对应位置的输出为：要想观测到更高的气温，至少需要等待的天数。如果气温在这之后都不会升高，请在该位置用 0 来代替。
 *
 * 例如，给定一个列表 temperatures = [73, 74, 75, 71, 69, 72, 76, 73]，你的输出应该是 [1, 1, 4, 2, 1, 1, 0, 0]。
 *
 * 初看题还没看懂
 *
 * 对于输入 73，它需要 经过一天 才能等到温度的升高，也就是在第二天的时候，温度升高到 74 ，所以对应的结果是 1。
 *
 * 对于输入 74，它需要 经过一天 才能等到温度的升高，也就是在第三天的时候，温度升高到 75 ，所以对应的结果是 1。
 *
 * 对于输入 75，它经过 1 天后发现温度是 71，没有超过它，继续等，一直 等了四天，在第七天才等到温度的升高，温度升高到 76 ，所以对应的结果是 4 。
 *
 * 对于输入 71，它经过 1 天后发现温度是 69，没有超过它，继续等，一直 等了两天，在第六天才等到温度的升高，温度升高到 72 ，所以对应的结果是 2 。
 *
 * 对于输入 69，它 经过一天 后发现温度是 72，已经超过它，所以对应的结果是 1 。
 *
 * 对于输入 72，它 经过一天 后发现温度是 76，已经超过它，所以对应的结果是 1 。
 *
 * 对于输入 76，后续 没有温度 可以超过它，所以对应的结果是 0 。
 *
 * 对于输入 73，后续 没有温度 可以超过它，所以对应的结果是 0 。
 *
 */
public class DailyTemperatures {

    public static int[] solution(int[] T){
        Deque<Integer> stack = new ArrayDeque<>();
        int[] result = new int[T.length];
        for (int i = 0 ; i < T.length ; i++ ){
            if (stack.isEmpty()){
                stack.addLast(i);
                continue;
            }
            Integer idx = stack.peekLast();
            //如果栈顶元素大于 当前元素进栈
            if (T[idx] > T[i]){
                stack.addLast(i);
                continue;
            }
            while (!stack.isEmpty()){
                idx = stack.peekLast();
                if (T[idx] >= T[i]){
                    break;
                }
                stack.pollLast();
                result[idx] = i - idx;
            }
            stack.addLast(i);

        }
        return result;
    }

    public static void main(String[] args) {
        int[] T = solution(new int[]{89,62,70,58,47,47,46,76,100,70});
        for (Integer x :T ){
            System.out.print(x + " ");

        }
    }
}
