package com.grvyframework.handle;

public interface IGrvyScriptResultHandler<P,R> {

    public R dealResult(P result);
}
