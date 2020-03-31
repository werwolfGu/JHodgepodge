package com.guce;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @Author chengen.gce
 * @DATE 2020/3/28 9:26 下午
 */
public class FunctionalAnnotation {

    private String key ;
    public String goodbye1(String name){
        return "goodbye" + name ;
    }

    public <R> R functionTest(Function<String,R> fun){
        return fun.apply(key);
    }

    public void comsumerTest (Consumer<String> con){
        con.accept(key);
    }

    public boolean predicateTest ( Predicate<String> predicate){
        return predicate.test(key);
    }

    public <R> R supplierTest(Supplier<R> supplier){
        return supplier.get();
    }

    public static void main(String[] args) {
        FunctionalAnnotation fa = new FunctionalAnnotation();
        Functional f = fa::goodbye1;
        Functional f1 =  name -> "goodbye," +name;

        System.out.println(f.goodbye("guce"));
        System.out.println(f1.goodbye("guce1"));

        fa.key = "function";
        String value = fa.functionTest(k -> {
            return "hello " + k;
        });
        System.out.println(value);

        fa.key = "consumer!";
        fa.comsumerTest( (key) -> System.out.println("hello " + key));

        System.out.println(fa.predicateTest(t -> {
            return Objects.isNull(t);
        }));
        value = fa.supplierTest( () -> {
            return "hello supplier";
        });

        System.out.println(value);
    }
}
