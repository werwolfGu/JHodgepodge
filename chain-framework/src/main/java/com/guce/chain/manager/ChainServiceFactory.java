package com.guce.chain.manager;

import com.guce.chain.IChainService;
import com.guce.chain.anno.ChainService;
import com.guce.chain.model.ChainRequest;
import com.guce.chain.model.ChainResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author chengen.gce
 * @DATE 2023/8/26 17:05
 */
@Component
@Slf4j
@ChainService(value = "service1", order = 600)
public class ChainServiceFactory implements IChainService {
    public ChainServiceFactory(){
        log.info("ChainServiceFactory init");
    }

    @Override
    public boolean handle(ChainRequest request, ChainResponse response) {
        return false;
    }
}
