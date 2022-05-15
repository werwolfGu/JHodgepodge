package com.guce.service.impl;

import com.guce.allocation.TradeDataEntity;
import com.guce.service.IAllocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2022/5/14 16:29
 */
@Service("allocationService")
@Slf4j
public class AllocationServiceImpl implements IAllocationService {

    @Override
    public void test(List<TradeDataEntity> list){
        log.info(Thread.currentThread().getName() + " invoker spring container mehtod....." + list.size());
    }
}
