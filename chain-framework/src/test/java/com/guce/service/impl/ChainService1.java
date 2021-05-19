package com.guce.service.impl;

import com.guce.chain.IChainService;
import com.guce.chain.anno.ChainService;
import com.guce.chain.model.ChainRequest;
import com.guce.chain.model.ChainResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author chengen.gu
 * @DATE 2020/2/13 3:02 下午
 */
@ChainService(value = "service1", order = 200, successSubResourceName = "subService1")
@Service
public class ChainService1 implements IChainService {

    private static Logger logger = LoggerFactory.getLogger(ChainService1.class);

    @Override
    public boolean handle(ChainRequest request, ChainResponse response) {

        System.out.println("test chainSerive1");

        return true;
    }

    @Override
    public void handleException(ChainRequest request, ChainResponse response ,Throwable throwable){
        logger.error("when failed do something......");
    }

    @Override
    public void doRollback(ChainRequest request, ChainResponse response ){
        System.out.println(" ChainService1 do roll back.......");
    }
}
