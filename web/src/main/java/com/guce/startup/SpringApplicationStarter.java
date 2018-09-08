package com.guce.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;


public class SpringApplicationStarter {

    private static Logger logger = LoggerFactory.getLogger(SpringApplicationStarter.class);
    public static void main(String[] args) {
        SpringApplication.run(SpringApplicationContainerBootstrap.class,args);

    }

    static class LogThread extends Thread{


        public void run(){
            long currTime = System.currentTimeMillis();
            long sysTime = System.currentTimeMillis();
            while (sysTime  - currTime <= 1000 * 10){
                sysTime = System.currentTimeMillis();
                for(int i = 0 ; i < 50 ; i++ ){
                    logger.info("log asyn test=========================== threadName:{} ; time: {}"
                            ,Thread.currentThread().getName(),sysTime);
                    logger.info("log asyn test=========================== threadName:{} ; time: {}"
                            ,Thread.currentThread().getName(),sysTime);
                }
                long castTime = System.currentTimeMillis() - sysTime;
                logger.info("threadName:{} ; cast time: {}"
                        ,Thread.currentThread().getName(),castTime);
            }

        }
    }
}
