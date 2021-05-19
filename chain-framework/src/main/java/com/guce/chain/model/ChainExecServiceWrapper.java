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

    /**
     * 执行chainResourceName
     */
    @Getter
    @Setter
    private String chainResourceName ;

    /**
     * 流程节点执行成功后，先执行子流程节点，再执行其他流程节点
     */
    @Getter
    @Setter
    private String successSubResourceName;

    /**
     * 流程节点执行异常后，先执行子流程节点，再执行后续流程节点，当然 当前节点不做结束流程和异常回滚
     */
    @Getter
    @Setter
    private String exceptionSubResourceName;


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

    @Setter
    @Getter
    private boolean isNeedNode = true;

    public void annoParamWrapper(ChainService anno){
        this.chainResourceName = anno.value();
        this.order = anno.order();
        this.isAsync = anno.isAsync();
        this.asyncTimeout = anno.asyncTimeout();
        this.isNeedNode = anno.isNeedNode();
        this.successSubResourceName = anno.successSubResourceName();
        this.exceptionSubResourceName = anno.exceptionSubResourceName();
    }

    @Override
    public String  toString(){

        return "{\"chainSerivce\":" + chainService
                + ", \"chainResourceName\":" + chainResourceName
                + ", \"order\":" + order
                + ", \"isAsync\":" + isAsync
                + ", \"asyncTimeout\":" + asyncTimeout
                + ", \"servicePath\":" + servicePath
                + ", \"isNeedNode\":" + isNeedNode + "}";
    }
}
