package com.guce.chain;

import com.guce.chain.anno.ChainService;
import com.guce.chain.model.ChainRequest;
import com.guce.chain.model.ChainResponse;
import org.springframework.stereotype.Service;

/**
 * @Author chengen.gce
 * @DATE 2020/2/23 11:16 上午
 */
@ChainService(value = "serviceDemo" ,order = 100)
@Service
public class ChainServiceDemo2 implements IChainService {
    @Override
    public boolean handle(ChainRequest request, ChainResponse response) {
        System.out.println("chain Service demo2 do something...");
        return false;
    }
}
