package com.grvyframework.reduce.impl;

import com.grvyframework.model.BaseScriptEvalResult;
import com.grvyframework.reduce.Reduce;

import java.util.function.Predicate;

/**
 * @author chengen.gce
 * @date 2020-01-21 13:39
 * @description
 */
public class ReduceAnyOf<T extends BaseScriptEvalResult> extends Reduce<T> {

    public ReduceAnyOf(Predicate predicate) {
        super(predicate);
    }

    @Override
    public boolean execute(T data){

        if (data != null && predicate.test(data)){
            result.add(data);
            return true;
        }
        return false;
    }
}
