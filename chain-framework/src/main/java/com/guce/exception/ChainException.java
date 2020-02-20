package com.guce.exception;

/**
 * @Author chengen.gu
 * @DATE 2020/2/13 2:37 下午
 */
public class ChainException extends RuntimeException {

    private String code;
    private String msg;

    public ChainException(String msg){
        super(msg);
    }

    public ChainException(String code,String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ChainException(ExceptionEnum exEnum){
        super();
    }

}
