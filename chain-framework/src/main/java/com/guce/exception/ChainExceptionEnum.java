package com.guce.exception;

import lombok.Getter;

/**
 * @Author chengen.gce
 * @DATE 2020/2/20 2:23 下午
 */
public enum ChainExceptionEnum {

    /**
     * 流程异常时，执行异常流程节点错误码
     */
    EXCEPTION_FLOW_NODE_EXECUTE("EX_FLOW_NODE_EXECUTE","流程异常时，执行异常流程节点");

    @Getter
    private String code;
    @Getter
    private String msg;

    ChainExceptionEnum(String code,String msg){
        this.code = code;
        this.msg = msg;
    }
}
