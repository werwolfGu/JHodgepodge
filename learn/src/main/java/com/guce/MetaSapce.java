package com.guce;


import javassist.ClassPool;

/**
 * @Author chengen.gce
 * @DATE 2020/4/6 10:24 上午
 */
public class MetaSapce {

    private static javassist.ClassPool cp = ClassPool.getDefault();
    public static void main(String[] args){

        /*JdkInvocationHandler invocationHandler = new JdkInvocationHandler();
        HelloDemo hello = new Hello();
        HelloDemo helloProxy = invocationHandler.getInstance(hello);
        System.out.println(helloProxy);
*/
        for ( long i = 0 ; ; i++){
            try {
                cp.makeClass("com.guce.proxy.Hello" + i).toClass();

            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }
}
