package com.guce;

import java.lang.reflect.InvocationTargetException;

/**
 * @Author chengen.gce
 * @DATE 2022/4/24 17:17
 */
public class ClassDemo {

    public static void main(String[] args) {
        try {
            String s = "0" ;
            Character[] c = new Character[]{'a','a'};
            Object obj = Class.forName("java.lang.Boolean").getDeclaredConstructor(String.class).newInstance("false");
            System.out.println(obj);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
