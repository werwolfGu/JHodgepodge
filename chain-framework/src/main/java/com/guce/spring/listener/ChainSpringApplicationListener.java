package com.guce.spring.listener;

import com.guce.chain.manager.ChainServiceManager;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Author chengen.gu
 * @DATE 2020/2/15 4:07 下午
 */
@Component
public class ChainSpringApplicationListener implements ApplicationListener<ContextRefreshedEvent> {


    private ChainServiceManager chainServiceManager ;

    public ChainSpringApplicationListener(ChainServiceManager chainServiceManager){

        this.chainServiceManager = chainServiceManager;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (contextRefreshedEvent.getApplicationContext().getParent() != null){
            chainServiceManager.init();
        }
    }
}
