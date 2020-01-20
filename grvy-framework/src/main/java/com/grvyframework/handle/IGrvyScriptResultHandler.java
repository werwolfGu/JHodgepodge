package com.grvyframework.handle;

/**
 * @author chengen.gu
 * @date 2020/1/21 8:11 下午
 */
public interface IGrvyScriptResultHandler {

    /**
     * 对表达式计算结果进行wrapper
     * @param result 表达式计算结果参数
     * @return  返回处理结果
     */
    public Object dealResult(Object result);
}
