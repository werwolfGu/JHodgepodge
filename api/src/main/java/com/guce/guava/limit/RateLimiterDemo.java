package com.guce.guava.limit;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @Author chengen.gce
 * @DATE 2020/6/20 4:57 下午
 */
public class RateLimiterDemo {

    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(10);
        rateLimiter.acquire();
    }
}
