package com.gce.spi.impl;

import com.gce.api.INumOperate;

/**
 * @Auther:chengen.gce
 * @Date:2019/5/25 13:29
 * @Description:
 */
public class NumSubtractOperatorImpl implements INumOperate {
    @Override
    public int operator(int a, int b) {
        return a - b;
    }
}
