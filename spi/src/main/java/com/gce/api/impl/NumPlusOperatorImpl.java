package com.gce.api.impl;

import com.gce.api.INumOperate;

/**
 * @Auther:chengen.gce
 * @Date:2019/5/25 13:27
 * @Description:
 */
public class NumPlusOperatorImpl implements INumOperate {
    @Override
    public int operator(int a, int b) {
        return a + b;
    }
}
