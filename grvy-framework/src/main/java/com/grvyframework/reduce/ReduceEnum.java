package com.grvyframework.reduce;

import lombok.Getter;

@Getter
public enum ReduceEnum {

    /**
     * 获取符合条件的第一个值
     */
    FIRST_OF("1","first of value", "获取符合条件的第一个值"),
    /**
     * 获取符合条件的任意一个值
     */
    ANY_OF("2","any of value","获取符合条件的任意一个值"),
    /**
     * 获取符合条件的所有值
     */
    ALL_OF("3","all of value","获取符合条件的所有值");

    String code;
    String msg;
    String comments;

    ReduceEnum(String code,String msg,String comments){
        this.code = code;
        this.msg = msg;
        this.comments = comments;
    }
}
