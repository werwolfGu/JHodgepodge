package com.guce.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author guchengen495
 * @date 2020-03-03 09:52
 * @description
 */
public class IdMaker64 {

    /**计数器*/
    private AtomicInteger counter = new AtomicInteger(0);

    /**随机数据类*/
    private Random r;

    /**编号*/
    private  char[] initId={'0'};




    private static int rand_limit=64*64*64*64;

    private static int count_limit=64*64*64;

    private int ipJump=1;

    private Object _syn = new Object();

    /**32进制代号*/
    private static  char datas[]={'0','1','2','3','4','5','6','7','8','9',
            'A','B','C','D','E','F','G','H','I','J','K',
            'L','M','N','O','P','Q','R','S','T','U','V','W',
            'X','Y','Z','a','b','c','d','e','f','g','h','i',
            'j','k','l','m','n','o','p','q','r','s','t','u',
            'v','w','x','y','z','A','B'};//64进制

    private static long mask = (1 << 6) - 1;

    public IdMaker64()
    {
        //-------------确认基点
        int ipInt= 0;
        InetAddress addr=null;
        try {
            addr = java.net.Inet4Address.getLocalHost();
            ipInt= addr.hashCode();
            ipJump= Math.abs(ipInt%64)+1;
        } catch (UnknownHostException e1) {
        }
        long seed= System.currentTimeMillis()+ipInt;
        //生成JVM编号
        r=new Random(seed);
        initId[0]=datas[r.nextInt(22)+10];
        //initId[1]=datas[r.nextInt(22)+10];
        //------------------------生成IP编号
    }


    /**生成ID*/
    private String make(){
        return _make();
    }

    private String _make() {
        char[] str=new char[]{'0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0'};
        //
        //-----计数器(3/4位)
        int count=getCount();
        //---------------------------
        long time= System.currentTimeMillis();
        //-----时间转为换32进制(6位)
        int charPos=8;
        do {
            str[charPos--] =datas[(int)(time & mask)];
            time >>>= 6;
        } while (time != 0);

        //--编入计数器
        charPos=str.length-5;
        do {
            str[charPos--] =datas[(int)(count & mask)];
            count >>>= 6;
        } while (count != 0);
        //--------------------编入jvm/ip(6位)
        str[0]=initId[0];
        int r_nums=r.nextInt(rand_limit);
        charPos=str.length-1;
        do {
            str[charPos--] =datas[(int)(r_nums & mask)];
            r_nums >>>= 6;
        } while (r_nums != 0);
        return new String(str);
    }



    /**计数器自+*/
    private  int getCount(){

        if (counter.get() < 0 || counter.get() >= count_limit){
            synchronized (this._syn){
                if (counter.get() < 0 || counter.get() >= count_limit){
                    counter.set(ipJump);
                }
            }
        }
        return counter.getAndAdd(ipJump);
    }

    public String getID() {
        return make();
    }


    public static void main(String[] args) {
        IdMaker64 idMaker64 = new IdMaker64();
        for (int i = 0; i < 100; i++)
            System.out.println(idMaker64.getID());
    }
}
