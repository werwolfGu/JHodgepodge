package com.guce.allocation.manager;

import com.guce.allocation.TradeDataEntity;
import com.guce.common.utils.NetUtils;
import com.guce.loadbalance.LoadBalance;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author chengen.gce
 * @DATE 2022/5/12 12:53
 */
@Slf4j
public class ClusterAllocationManager{
    @Getter
    @Setter
    private static List<String> clusterServerList = new ArrayList<>(128);
    private LoadBalance loadBalance ;
    private boolean allMsgPollMode;



    private static final String host = NetUtils.getLocalHost();

    public ClusterAllocationManager (LoadBalance loadBalance) {
        this(false,loadBalance);
    }
    public ClusterAllocationManager (boolean allMsgPollMode,LoadBalance loadBalance) {
        this.allMsgPollMode = allMsgPollMode;
        this.loadBalance = loadBalance;
    }

    /**
     * 通过负载均衡算法选择执行的服务器
     * @param tradeList
     * @return
     */
    public List<TradeDataEntity> selectExecuteJVM(List<? extends TradeDataEntity> tradeList){
        if (CollectionUtils.isEmpty(tradeList)) {
            return null;
        }
        List<String> list = clusterServerList;
        return tradeList.stream().filter( data -> {
            if (!allMsgPollMode) {
                return true;
            }
            if (data.getClusterIdentifierCode() == null) {
                log.error("该数据无法被识别是那台机器跑的，取消执行:{}" ,data);
                return false;
            }
            String select = loadBalance.select(list,data.getClusterIdentifierCode().toString());
            if (host.equals(select)) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
    }

    public  void simulateClusterServer () {
        List<String> invokers = new ArrayList<>();
        invokers.add("192.168.1.5");
        invokers.add("192.168.1.6");
        invokers.add("192.168.1.7");
        invokers.add("192.168.1.8");
        invokers.add("192.168.1.9");
        invokers.add("192.168.1.20");
        invokers.add("192.168.31.196");
        invokers.add("22.9.171.133");
        ClusterAllocationManager.getClusterServerList().addAll(invokers);
    }
}
