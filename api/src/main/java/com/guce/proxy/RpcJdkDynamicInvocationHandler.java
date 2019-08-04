package com.guce.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by chengen.gu on 2018/11/2.
 */
public class RpcJdkDynamicInvocationHandler implements InvocationHandler {

    private String host;
    private int port;

    /**
     * <p>
     *     rpc 动态代理 客户端直接调用接口就能找到服务端实现类 ，主要是通过接口动态代理实现；再加以socket链接到服务器；
     *     主要使用到的技术有  动态代理、socket编程(netty)、反射；
     *
     * </p>
     * @param interfaceClass
     * @param <T>
     * @return
     */
    public <T> T refer(final Class<T> interfaceClass,String host,int port){

        this.host = host;
        this.port = port;
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class<?>[]{interfaceClass},this);
    }

    /**
     * 客户端通过发送 接口名  方法名  参数类型  参数名称； 服务端接收到后通过反射执行对应的方法；返回结果
     * @param proxy     执行的类
     * @param method    执行的方法
     * @param args      传入的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //netty 链接到远程客户端 进行通讯
        String methodName = method.getName();
        Class[] clazzType = method.getParameterTypes();

        return null;
    }
}
