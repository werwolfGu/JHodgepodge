package com.guce;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ){

        System.out.println( "Hello World!" );
        String a = new String("abc");
        String b = new String("abc");
        System.out.println(a.hashCode());
        System.out.println(b.hashCode());
        System.out.println(System.identityHashCode(b));
        System.out.println(System.identityHashCode(a));
        System.out.println(a == b);
    }
}
