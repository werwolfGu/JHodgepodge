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
import java.util.concurrent.ExecutionException;

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
        try {
            CompletableFuture future = CompletableFuture.runAsync(() -> simulateBusiness("business1" , 0,10000,4));
            CompletableFuture future1 = CompletableFuture.runAsync(() -> simulateBusiness("business2" , 0,10000,8));

            CompletableFuture[] arr = new CompletableFuture[]{future,future1};
            CompletableFuture.allOf(arr).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    public void simulateBusiness(final String businessName ,final int min  ,final int max,int concurrentThreadNumber) {

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
                }).msgPollInvoker((param , consumerThreadService) -> {
                    int segment = 400, currMin = min;
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
                        consumerThreadService.allocationToConsumerThreadQueue(list);
                    }
                    return true;
                }).build();

        ClusterDispatchManager allocationManager = clusterAllocationBuilder.buildClusterAllocation();
        allocationManager.simulateClusterServer();

        CompletableFuture future = clusterAllocationBuilder
                .buildClusterConsumerThreadsManager().startupBusinessHandleThread();

        CompletableFuture pollMsgFuture = clusterAllocationBuilder.startupMsgPollService(null);
        try {
            CompletableFuture[] arr = new CompletableFuture[]{future,pollMsgFuture};
            CompletableFuture.allOf(arr).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
