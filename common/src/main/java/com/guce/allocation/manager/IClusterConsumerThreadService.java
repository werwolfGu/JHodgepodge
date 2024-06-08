package com.guce.allocation.manager;

import com.guce.allocation.TradeDataEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @Author chengen.gce
 * @DATE 2022/5/14 16:40
 */
public interface IClusterConsumerThreadService {
    public CompletableFuture startupBusinessHandleThread();
    public CompletableFuture startupBusinessMsgPoll(Consumer<Map<String,Object>> mapConsumer);
    public void allocationToConsumerThreadQueue(List<TradeDataEntity> list);

    public CompletableFuture startupBusinessMsgPoll(final Map<String,Object> param ,BiFunction<Map<String,Object>,IClusterConsumerThreadService,Boolean> function);
}
