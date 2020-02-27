package com.guce.chain.model;

import com.guce.chain.IChainService;
import com.guce.chain.anno.ChainService;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author chengen.gu
 * @DATE 2020/2/14 1:49 下午
 */
public class ChainExecServiceWrapper {

    /**
     * 执行service
     */
    @Getter
    @Setter
    private IChainService chainService;

    @Setter
    @Getter
    private IChainService otherChainService;
    /**
     * 执行chainResourceName
     */
    @Getter
    @Setter
    private String chainResourceName ;

    /**
     * 执行顺序
     */
    @Getter
    @Setter
    private int order;

    /**
     * 是否异步
     */
    @Getter
    @Setter
    private boolean isAsync;

    /**
     * 异步超时时间
     */
    @Getter
    @Setter
    private long asyncTimeout;

    @Setter
    @Getter
    private String servicePath;

    @Getter
    @Setter
    private String otherServicePath;

    @Setter
    @Getter
    private boolean isNeedNode = true;

    public void annoParamWrapper(ChainService anno){
        this.chainResourceName = anno.value();
        this.order = anno.order();
        this.isAsync = anno.isAsync();
        this.asyncTimeout = anno.asyncTimeout();
        this.isNeedNode = anno.isNeedNode();
    }

    @Override
    public String  toString(){

        return "{\"chainSerivce\":" + chainService
                + ", \"chainResourceName\":" + chainResourceName
                + ", \"order\":" + order
                + ", \"isAsync\":" + isAsync
                + ", \"asyncTimeout\":" + asyncTimeout
                + ", \"servicePath\":" + servicePath
                + ", \"otherService\":" + otherServicePath
                + ", \"isNeedNode\":" + isNeedNode + "}";
    }
}
