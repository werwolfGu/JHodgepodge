package com.grvyframework.exception;

/**
 * @Author chengen.gu
 * @DATE 2020/1/20 10:29 下午
 */
public class GrvyExecutorException extends RuntimeException {

    private String code;
    private String msg;


    public GrvyExecutorException(String code ,String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }
    public GrvyExecutorException(GrvyExceptionEnum grvyEnum) {
        super(grvyEnum.getMsg());
        this.code = grvyEnum.getCode();
        this.msg = grvyEnum.getMsg();
    }

    public GrvyExecutorException(GrvyExceptionEnum grvyEnum, Throwable cause) {
        super(grvyEnum.getMsg(), cause);
        this.code = grvyEnum.getCode();
        this.msg = grvyEnum.getMsg();
    }

    public GrvyExecutorException(String code,String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.msg = message;
    }

}
