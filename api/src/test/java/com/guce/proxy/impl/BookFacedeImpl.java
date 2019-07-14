package com.guce.proxy.impl;

import com.guce.proxy.CgLibInvocationHandler;
import com.guce.proxy.IBookFacade;
import com.guce.proxy.JdkInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by chengen.gu on 2018/10/10.
 */
public class BookFacedeImpl implements IBookFacade {
    @Override
    public String addBook() {
        System.out.println("add book");
        return "java";
    }

    public static void main(String[] args) {

        IBookFacade bookFacade = new BookFacedeImpl();
        JdkInvocationHandler jdkInvocationHandler = new JdkInvocationHandler();
        IBookFacade bookFacade1 = (IBookFacade)jdkInvocationHandler.getInstance(bookFacade);
        bookFacade1.addBook();

        CgLibInvocationHandler cgLibInvocationHandler = new CgLibInvocationHandler();
        BookFacedeImpl bookFacede2 = (BookFacedeImpl) cgLibInvocationHandler.getInstance(new BookFacedeImpl());
        bookFacede2.addBook();

        Proxy.newProxyInstance(IBookFacade.class.getClassLoader(),new Class[]{IBookFacade.class},new InvocationHandler(){

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        });
    }
}
