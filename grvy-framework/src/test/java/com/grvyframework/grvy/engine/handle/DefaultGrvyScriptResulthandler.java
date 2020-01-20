package com.grvyframework.grvy.engine.handle;

import com.grvyframework.handle.IGrvyScriptResultHandler;
import org.springframework.stereotype.Component;

/**
 * @author chengen.gu
 * @date 2020-01-20 17:23
 * @description
 */
@Component
public class DefaultGrvyScriptResulthandler implements IGrvyScriptResultHandler<String,String> {
    @Override
    public String dealResult(String result) {
        System.out.println("test default handle");
        return result;
    }
}
