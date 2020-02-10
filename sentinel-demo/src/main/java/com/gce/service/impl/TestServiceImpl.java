package com.gce.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.gce.service.ExceptionUtil;
import com.gce.service.ITestService;
import org.springframework.stereotype.Service;

/**
 * @Author chengen.gu
 * @DATE 2020/2/4 1:05 下午
 */
@Service
public class TestServiceImpl implements ITestService {
    @Override
    @SentinelResource(value = "test", blockHandler = "handleException", blockHandlerClass = {ExceptionUtil.class})
    public void test() {

        System.out.println("Test");
    }

    @Override
    @SentinelResource(value = "hello", fallback = "helloFallback")
    public String hello(long s) {

        if (s < 0) {
            throw new IllegalArgumentException("invalid arg");
        }
        return String.format("Hello at %d", s);
    }

    @Override
    @SentinelResource(value = "helloAnother", defaultFallback = "defaultFallback",
            exceptionsToIgnore = {IllegalStateException.class})
    public String helloAnother(String name) {
        if (name == null || "bad".equals(name)) {
            throw new IllegalArgumentException("oops");
        }
        if ("foo".equals(name)) {
            throw new IllegalStateException("oops");
        }
        return "Hello, " + name;
    }

    public String helloFallback(long s, Throwable ex) {
        // Do some log here.
        ex.printStackTrace();
        return "Oops, error occurred at " + s;
    }

    public String defaultFallback() {
        System.out.println("Go to default fallback");
        return "default_fallback";
    }
}
