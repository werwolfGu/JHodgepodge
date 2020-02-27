package com.guce.exception;

import lombok.Getter;

/**
 * @Author chengen.gu
 * @DATE 2020/2/13 2:37 下午
 */
public class ChainException extends RuntimeException {

    @Getter
    private String code;
    @Getter
    private String msg;

    public ChainException(String msg){
        super(msg);
    }

    public ChainException(String code,String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ChainException(ChainExceptionEnum exEnum){
        super();
    }

}
