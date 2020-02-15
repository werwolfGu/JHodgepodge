package com.guce.spring.listener;

import com.guce.chain.executor.ChainExecutor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Author chengen.gu
 * @DATE 2020/2/15 4:07 下午
 */
@Component
public class ChainSpringApplicationListener implements ApplicationListener<ContextRefreshedEvent> {


    private ChainExecutor chainExecutor ;

    public ChainSpringApplicationListener(ChainExecutor executor){

        this.chainExecutor = executor;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (contextRefreshedEvent.getApplicationContext().getParent() != null){
            chainExecutor.init();
        }
    }
}
