package com.guce.exception;

/**
 * @Author chengen.gu
 * @DATE 2020/2/13 2:37 下午
 */
public class ChainException extends RuntimeException {

    public ChainException(String msg){
        super(msg);
    }
}
