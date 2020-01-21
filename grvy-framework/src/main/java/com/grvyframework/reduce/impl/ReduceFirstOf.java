package com.grvyframework.reduce.impl;

import com.grvyframework.reduce.Reduce;

import java.util.function.Predicate;

/**
 * @author chengen.gce
 * @date 2020-01-21 13:38
 * @description
 */
public class ReduceFirstOf<T> extends Reduce<T> {


    public ReduceFirstOf(Predicate predicate) {
        super(predicate);
    }

    @Override
    public boolean execute(T data){

        if (predicate.test(data)){
            result.add(data);
            return true;
        }
        return false;
    }

}
