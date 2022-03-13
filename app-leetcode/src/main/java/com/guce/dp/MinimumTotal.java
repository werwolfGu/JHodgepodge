package com.guce.dp;

import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2021/8/1 9:59 上午
 */
public class MinimumTotal {

    public int minimumTotal(List<List<Integer>> triangle) {

        int rows = triangle.size();

        return triangle.get(0).get(0);
    }
}
