package com.grvyframework.exception;

import lombok.Getter;

/**
 * @Author chengen.gu
 * @DATE 2020/1/20 10:46 下午
 */
public enum GrvyExceptionEnum {

    /**
     * groovy 执行脚本为空
     */
    GRVY_SCRIPT_EMPTY("40000001","grvy_script_empty","groovy 执行脚本为空"),
    /**
     * groovy 执行异常
     */
    GRVY_EXECUTOR_ERROR("40000002","grvy_executor_error","groovy 执行异常"),
    /**
     * grvy参数异常
     */
    Grvy_ILLEGAL_ARGUMMENT_ERROR("40000004","grvy_illegalment_argument_error","请求参数异常"),
    /**
     * groovy 未知异常
     */
    GRVY_EXECUTOR_UNKNOWN_ERROR("40000003","grvy_executor_unknown_error","groovy 未知异常");

    @Getter
    private String code;
    @Getter
    private String msg;
    @Getter
    private String comments;

    GrvyExceptionEnum(String code,String msg,String comments){
        this.code = code;
        this.msg = msg;
        this.comments = comments;
    }
}
