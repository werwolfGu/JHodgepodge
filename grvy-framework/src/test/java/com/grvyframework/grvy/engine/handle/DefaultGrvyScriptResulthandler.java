package com.grvyframework.grvy.engine.handle;

import com.grvyframework.handle.IGrvyScriptResultHandler;
import com.grvyframework.model.BaseScriptEvalResult;
import com.grvyframework.model.BaseScriptEvalResultCalculateParam;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author chengen.gu
 * @date 2020-01-20 17:23
 * @description
 */
@Component
public class DefaultGrvyScriptResulthandler implements IGrvyScriptResultHandler {

    @Override
    public BaseScriptEvalResult dealResult(Object result, BaseScriptEvalResultCalculateParam calculateParam) {

        Boolean flag = Optional.ofNullable(result)
                .map( Object::toString)
                .map(Boolean::parseBoolean)
                .orElse(Boolean.FALSE);

        if ( flag ){

            //throw new IllegalArgumentException("BaseScriptEvalResultCalculateParam 不能为空!");
        }
        BaseScriptEvalResult evalResult = new BaseScriptEvalResult();
        if (flag && calculateParam != null){

            evalResult.setAmt(calculateParam.getAmt());
            evalResult.setAmt(1L);
        }
        return evalResult;
    }
}
