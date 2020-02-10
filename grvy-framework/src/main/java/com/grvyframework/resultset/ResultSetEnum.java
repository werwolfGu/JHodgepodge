package com.grvyframework.resultset;

import lombok.Getter;

/**
 * @Author chengen.gu
 * @DATE 2020/2/10 5:38 下午
 */

public enum ResultSetEnum {

    /**
     * 只取一个结果
     */
    FIRSTOF(1,"只取一个"),
    /**
     * 获取所有结果集
     */
    ALLOF(2,"获取所有结果"),
    /**
     * 获取任意一个结果集
     */
    ANYOF(3,"任意一个结果集");

    @Getter
    private Integer code;
    @Getter
    private String msg;
    ResultSetEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }
}
