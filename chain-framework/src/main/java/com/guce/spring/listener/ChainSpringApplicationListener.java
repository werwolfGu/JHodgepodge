package com.guce.spring.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @Author chengen.gu
 * @DATE 2020/2/15 4:07 下午
 */
public class ChainSpringApplicationListener implements ApplicationListener<ContextRefreshedEvent> {


    private static final Logger logger = LoggerFactory.getLogger(ChainSpringApplicationListener.class);

    private static volatile boolean spring_loader_finished = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (event.getApplicationContext().getParent() == null){

            spring_loader_finished = true;
            logger.warn(">>>>>>spring 加载完成！！！<<<<<<");
        }

    }

    public static boolean isSpringLoaderFinished(){
        return spring_loader_finished;
    }


}
