package com.guce.service.impl;

import com.guce.chain.IChainService;
import com.guce.chain.anno.ChainService;
import com.guce.chain.model.ChainRequest;
import com.guce.chain.model.ChainResponse;
import org.springframework.stereotype.Service;

/**
 * @Author chengen.gce
 * @DATE 2021/3/4 1:38 下午
 */
@ChainService(value = "subService1",order = 100)
@Service
public class SubServiceImpl2 implements IChainService {
    @Override
    public boolean handle(ChainRequest request, ChainResponse response) {

        System.out.println("执行子流程2");
        return false;
    }
}
