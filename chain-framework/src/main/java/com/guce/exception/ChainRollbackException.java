package com.guce.exception;

/**
 * @Author chengen.gu
 * @DATE 2020/2/13 5:50 下午
 */
public class ChainRollbackException extends RuntimeException {

    public ChainRollbackException(String msg){
        super(msg);
    }
}
