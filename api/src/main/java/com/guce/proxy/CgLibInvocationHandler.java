package com.guce.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by chengen.gu on 2018/10/10.
 */
public class CgLibInvocationHandler implements MethodInterceptor {

    private static Logger logger = LoggerFactory.getLogger(CgLibInvocationHandler.class);

    private Object target;

    public Object getInstance(Object target){

        this.target = target;

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        enhancer.setCallback(this);

        return enhancer.create();
    }


    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        System.out.println("cglib dynamic proxy start...");
//        methodProxy.invokeSuper(target, args); //调用业务类（父类中）的方法
        methodProxy.invoke(target,args);
        System.out.println("cglib dynamic proxy end...");
        return null;
    }
}
