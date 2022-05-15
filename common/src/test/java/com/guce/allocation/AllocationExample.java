package com.guce.allocation;

import com.guce.allocation.manager.ClusterDispatchManager;
import com.guce.loadbalance.impl.ConsistentHashLoadBalance;
import com.guce.service.IAllocationService;
import com.guce.service.impl.AllocationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Author chengen.gce
 * @DATE 2022/5/14 16:21
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AllocationServiceImpl.class})
public class AllocationExample {

    @Resource(name = "allocationService")
    private IAllocationService loggerTraceTestService;
    @Test
    public void test () {
        String businessName = "queueName";
        int concurrentThreadNumber = 4;
        int min = 0 , max = 10000;

        ClusterAllocationBuilder clusterAllocationBuilder = ClusterAllocationBuilder.builder()
                ////业务名称  用于区分执行的是哪个业务
                .businessName(businessName)
                //////批量处理数量
                .batchSize(100)
                /////是否开启全量数据拉去模式
                .allMsgPollMode(true)
                /////并发线程数
                .threadConcurrentNumber(concurrentThreadNumber)
                /////负载均衡算法
                .threadLoadBalance(new ConsistentHashLoadBalance())
                .serverLoadBalance(new ConsistentHashLoadBalance())
                /////业务线程中断循环，终止线程执行
                .interruptedBusinessThreadLoopInvoker( ()-> false)
                //////业务逻辑实现
                .businessInvoker(list  -> {
                    loggerTraceTestService.test(list);
                    return true;
                }).build();

        ClusterDispatchManager allocationManager = clusterAllocationBuilder.buildClusterAllocation();
        allocationManager.simulateClusterServer();

        CompletableFuture future = clusterAllocationBuilder
                .buildClusterConsumerThreadsManager().startupBusinessHandleThread();

        clusterAllocationBuilder
                .buildClusterConsumerThreadsManager().startupBusinessMsgPoll( map -> {

                    int segment = 100, currMin = min;
                    while (true) {
                        int start = currMin ;
                        int end = start + segment;
                        currMin = end;
                        if (start > max) {
                            break;
                        }
                        end = Math.min(end,max);
                        List<TradeDataEntity> list = new ArrayList<>(100);
                        for (int i = start ; i < end ; i++) {
                            TradeDataEntity<String> data = new TradeDataEntity();
                            data.setBusinessName(businessName);
                            data.setClusterIdentifierCode(Long.valueOf(i));
                            data.setThreadIdentifierCode(String.valueOf(i));
                            list.add(data);
                        }

                        clusterAllocationBuilder
                                .buildClusterConsumerThreadsManager().allocationToConsumerThreadQueue(list);
                    }
                });
        try {
            future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
