package com.grvyframework.handle.impl;

import com.grvyframework.handle.IGrvyScriptResultHandler;
import com.grvyframework.model.BaseScriptEvalResult;
import com.grvyframework.model.BaseScriptEvalResultCalculateParam;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author chengen.gce
 * @date 2020-01-21 09:20
 * @description
 */
@Component("defaultGrvyScriptResultHandler")
public class DefaultGrvyScriptResultHandler implements IGrvyScriptResultHandler {

    @Override
    public BaseScriptEvalResult dealResult(Object result, BaseScriptEvalResultCalculateParam calculateParam) {

        Boolean flag = Optional.ofNullable(result)
                .map( Object::toString)
                .map(Boolean::parseBoolean)
                .orElse(Boolean.FALSE);

        BaseScriptEvalResult evalResult = new BaseScriptEvalResult();
        if (flag && calculateParam != null){

            evalResult.setAmt(calculateParam.getAmt());
        }
        return evalResult;
    }
}
