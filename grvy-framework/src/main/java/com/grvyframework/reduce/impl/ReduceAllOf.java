package com.grvyframework.reduce.impl;

import com.grvyframework.model.BaseScriptEvalResult;
import com.grvyframework.reduce.Reduce;

import java.util.function.Predicate;

/**
 * @author chengen.gce
 * @date 2020-01-21 13:38
 * @description
 */
public class ReduceAllOf<T extends BaseScriptEvalResult> extends Reduce<T> {

    public ReduceAllOf(Predicate predicate) {
        super(predicate);
    }


    @Override
    public boolean execute(T data){

        if (predicate.test(data)){
            result.add(data);
            return false;
        }
        return false;
    }
}
