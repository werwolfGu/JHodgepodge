package com.guce.service.impl;

import com.guce.annotation.LoggerTrace;
import com.guce.annotation.SwitchCache;
import com.guce.annotation.SwitchCacheClient;
import com.guce.annotation.ThreadPoolClient;
import com.guce.service.LoggerTraceTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
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
    @SwitchCache(keys = {"key1", "key2", "key3", "key4"},
            valueTypes = {String.class, Set.class, List.class})
    public void test() {
        System.out.println("test");

        String value1 = SwitchCacheClient.getSwtichValue("key1");
        Set<String> val2 = SwitchCacheClient.getSwtichValue("key2");
        List<String> val3 = SwitchCacheClient.getSwtichValue("key3");
        String val4 = SwitchCacheClient.getSwtichValue("key4");
        System.out.println("key1  " + value1);
        System.out.println("key2  " + val2);
        System.out.println("key3  " + val3);
        System.out.println("key4  " + val4);
        CompletableFuture future = CompletableFuture
                .runAsync(() -> System.out.println("thread pool test"), executorService);
        try {
            future.get();
        } catch (Exception e) {
            log.error("error", e);
        }
    }
}
