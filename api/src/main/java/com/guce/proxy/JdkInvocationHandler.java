package com.guce.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by chengen.gu on 2018/10/10.
 */
public class JdkInvocationHandler implements InvocationHandler {

    private Logger logger = LoggerFactory.getLogger(JdkInvocationHandler.class);

    private Object target;

    public <T> T getInstance(T target){

        this.target = target;
        return (T)Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
    }



    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object result = null;
        logger.info("代理前。。。。。");
        System.out.println("jdk dynamic proxy start...");
        //调用业务方法
        result = method.invoke(target,args);
        System.out.println("jdk dynamic proxy end...");
        logger.info("代理后。。。。。");
        return result;
    }

    public static void main(String[] args) {
        JdkInvocationHandler invocationHandler = new JdkInvocationHandler();
        Hello hello = new Hello();
        HelloDemo helloProxy = invocationHandler.getInstance(hello);
        helloProxy.say();

    }
}
