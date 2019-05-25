package com.gce;

import com.gce.api.INumOperate;
import com.gce.api.impl.NumPlusOperatorImpl;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @Auther:chengen.gce
 * @Date:2019/5/25 13:31
 * @Description:
 */
public class SpiServiceMain {

    public static void main(String[] args) {
        //api
        INumOperate plus = new NumPlusOperatorImpl();
        System.out.println(plus.operator(4,5));

        //spi
        ServiceLoader<INumOperate> loader = ServiceLoader.load(INumOperate.class);
        Iterator<INumOperate> iterator = loader.iterator();

        INumOperate numOperate = null;
        while (iterator.hasNext()){
            numOperate = iterator.next();
            System.out.println(numOperate.operator(4,5));
        }
    }
}
