package com.guce.chain;


import com.guce.chain.model.ChainRequest;
import com.guce.chain.model.ChainResponse;

/**
 * @Author chengen.gu
 * @DATE 2020/2/13 2:13 下午
 */
public interface IChainService {

    /**
     * 方法返回  true 继续走下面的service  false ：不再继续后面的流程
     * @param request
     * @param response
     * @return
     */
    public boolean handle(ChainRequest request, ChainResponse response);

    default public void doSuccess(ChainRequest request, ChainResponse response){}

    default public void handleException(ChainRequest request, ChainResponse response , Throwable throwable){}

    default public void doComplated(ChainRequest request, ChainResponse response){}

    default public void doRollback(ChainRequest request, ChainResponse response){}

}
