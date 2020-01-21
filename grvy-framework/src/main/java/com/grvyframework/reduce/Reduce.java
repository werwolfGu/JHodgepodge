package com.grvyframework.reduce;

import com.grvyframework.reduce.impl.ReduceAllOf;
import com.grvyframework.reduce.impl.ReduceAnyOf;
import com.grvyframework.reduce.impl.ReduceFirstOf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author guchengen495
 * @date 2020-01-21 13:36
 * @description
 */
public abstract class Reduce<T> {

    protected Predicate predicate;

    protected List<T> result = new ArrayList<>();

    public Reduce(Predicate predicate){
        this.predicate = predicate;
    }

    /**
     * 获取结果判断
     * @param data
     * @return
     */
    public abstract boolean execute(T data);

    public List<T> getResult(){

        return result;
    }

    public static <V> Reduce<V> firstOf(Predicate<V> predicate){

        return new ReduceFirstOf(predicate);
    }

    public static <V> Reduce<V> anyof(Predicate<V> predicate){
        return new ReduceAnyOf(predicate);
    }

    public static Reduce allof(Predicate predicate){
        return new ReduceAllOf(predicate);
    }

    public static void main(String[] args) {

        Reduce<String> first = Reduce.firstOf(Objects::nonNull);
        first.execute("a");
        System.out.println(first.getResult());

        Reduce<String> allof = Reduce.allof(Objects::nonNull);
        for (int i = 0 ; i < 10 ; i++ ){
            if ( allof.execute(String.valueOf(i))){
                break;
            }
        }
        System.out.println(allof.getResult());

        Reduce<String> anyof = Reduce.anyof(Objects::nonNull);
        for (int i = 0 ; i < 10 ; i++ ){
            if (anyof.execute(String.valueOf(i))){
                break;
            }
        }
        System.out.println(anyof.getResult());
    }
}
