package com.guce.loadbalance;

import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2022/5/10 15:10
 */
public interface LoadBalance {

    public <T> T select (List<T> invokers , String invocation);
}
