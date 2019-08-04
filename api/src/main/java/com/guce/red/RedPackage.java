package com.guce.red;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by chengen.gu on 2018/10/18.
 * 红包算法
 * 单位为分
 */
public class RedPackage {

    //红包最小值  1分
    private final static int MIN_MONEY = 1;

    //红包最大值  200元
    private final static int MAX_MONEY = 200 * 100;

    //最大红包因子
    private final static float MAX_RED_PACKAGE = 0.23f;

    private final static int LESS = -1 ;

    private final static int MORE = -2;

    private final static int OK = 1;

    private int recursiveCount = 0;


    public List<Integer> spiltRedPackage(int money,int count){

        List<Integer> redList = new ArrayList<>(count);

        if(count < 1){
            throw new IllegalArgumentException("红包个数不能少于1");
        }

        if(count == 1){
            redList.add(money);
            return redList;
        }

        //红包的最大值
        int maxRedPackage = (int) (money * MAX_RED_PACKAGE);

        for(int i = 0 ; i < count ; i++ ){

            //获取随机红包值
//            int randomMoney = randomRedPacket_1(money,MIN_MONEY,maxRedPackage,count - i);
            int randomMoney = randomRedPacket_2(money,count - i);
            if(randomMoney == 0){
                continue;
            }
            redList.add(randomMoney);

            //总金额减少
            money -= randomMoney;
        }
//        Collections.shuffle(redList);
        return redList;
    }


    /**
     * 随机数红包会出现的一个问题就是 后面的红包会越来越小； 前面的红包一般情况下都是更大的 ；
     * @param totalMoney
     * @param minMoney
     * @param maxMoney
     * @param count
     * @return
     */
    public int randomRedPacket(int totalMoney,int minMoney,int maxMoney,int count){
        if(count == 1){
            return totalMoney;
        }

        if(minMoney == maxMoney){
            return minMoney;
        }

        //如果最大金额大于了剩余金额 则用剩余金额 因为这个 money 每分配一次都会减小
        maxMoney = maxMoney > totalMoney ? totalMoney : maxMoney ;

        //在 minMoney到maxMoney 生成一个随机红包
        int redPacket = (int) (Math.random() * (maxMoney - minMoney) + minMoney) ;

        int lastMoney = totalMoney - redPacket ;
        int status = checkMoney(lastMoney,count - 1);
        if(OK == status){
            return redPacket;
        }

        if(LESS == status){
            recursiveCount++;
            System.out.println("recursiveCount==" + recursiveCount);
            return randomRedPacket(totalMoney,minMoney,redPacket,count) ;
        }

        if (MORE == status){
            recursiveCount ++ ;
            System.out.println("recursiveCount===" + recursiveCount);
            return randomRedPacket(totalMoney,redPacket,maxMoney,count) ;
        }

        return redPacket;

    }


    public int randomRedPacket_1(int totalMoney,int minMoney,int maxMoney,int count){

        if(count <= 0 ){
            return 0;
        }

        if(count == 1 ){
            return totalMoney;
        }

        if(minMoney == maxMoney){
            return totalMoney;
        }

        int currRandomMoney = ThreadLocalRandom.current().nextInt(minMoney,maxMoney);
        int lastMoney = totalMoney - currRandomMoney;
        int status = checkMoney(lastMoney , count - 1);

        if(LESS == status){
            currRandomMoney = randomRedPacket_1(totalMoney,minMoney,currRandomMoney,count);
        }else if(MORE == status ){
            currRandomMoney = randomRedPacket_1(totalMoney,currRandomMoney,maxMoney,count);

        }


        return currRandomMoney;
    }


    /**
     * 目前这个算法最平均
     *  如果是浮点数的话  使用 BigDecimal
     *  取 平均红包大小*2 的随机数 作为本次红包的大小
     * @param remainMoney   剩余money
     * @param remainSize    剩余红包数
     * @return
     */
    public int randomRedPacket_2(int remainMoney,int remainSize){

        if(remainSize == 1){
            return remainMoney;
        }

        int send = remainMoney / remainSize * 2;
        int money = ThreadLocalRandom.current().nextInt(send);
        money = money <= MIN_MONEY ? MIN_MONEY : money;
        //check 剩余的钱 是否能分配给剩余的红包个数
        int status = checkMoney(remainMoney - money ,remainSize - 1);
        if(OK != status){
            recursiveCount++;
            System.out.println("recursiveCount:" + recursiveCount);
            money = randomRedPacket_2(remainMoney,remainSize);
        }
        return money;
    }
    /**
     * 校验剩余的金额的平均值是否在 最小值和最大值这个范围内
     * @param lastMoney
     * @param count
     * @return
     */
    private int checkMoney(int lastMoney, int count) {
        double avg = lastMoney / count ;
        if (avg < MIN_MONEY){
            return LESS ;
        }

        if (avg > MAX_MONEY){
            return MORE ;
        }

        return OK ;
    }

    public static void main(String[] args) {
        for(int i = 0  ; i< 10 ; i++) {
            RedPackage redPackage = new RedPackage();
            List<Integer> list = redPackage.spiltRedPackage(2000,20);
            int sum = 0 ;
            for(int s : list){
                sum += s;
            }
            System.out.println("sum:"+ sum + " ;" + list);
        }

    }
}
