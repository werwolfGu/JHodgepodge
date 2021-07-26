package com.guce.service.impl;

import com.guce.annotation.LoggerTrace;
import com.guce.annotation.ThreadPoolClient;
import com.guce.service.LoggerTraceTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * @Author chengen.gce
 * @DATE 2021/7/26 10:19 下午
 */
@Service
@Slf4j
public class LoggerTraceTestServiceImpl implements LoggerTraceTestService {

    @ThreadPoolClient
    private ExecutorService executorService;

    @Override
    @LoggerTrace
    public void test() {
        System.out.println("test");
        CompletableFuture future = CompletableFuture
                .runAsync( () -> System.out.println("thread pool test"),executorService);
        try {
            future.get();
        } catch (Exception e) {
            log.error("error",e);
        }
    }
}
