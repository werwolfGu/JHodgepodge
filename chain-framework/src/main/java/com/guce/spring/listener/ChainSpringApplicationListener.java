package com.guce.spring.listener;

import com.guce.chain.IChainService;
import com.guce.chain.manager.ChainServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @Author chengen.gu
 * @DATE 2020/2/15 4:07 下午
 */
public class ChainSpringApplicationListener implements ApplicationListener<ContextRefreshedEvent> {


    private static Logger logger = LoggerFactory.getLogger(ChainSpringApplicationListener.class);
    private ChainServiceManager chainServiceManager ;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        String[] beanNames = contextRefreshedEvent.getApplicationContext().getBeanNamesForType(IChainService.class);
        chainServiceManager = contextRefreshedEvent.getApplicationContext().getBean(ChainServiceManager.class);
        if (beanNames != null && beanNames.length > 0 && chainServiceManager != null){
            logger.warn("spring listener init chain flow service! start");
            chainServiceManager.init();
            logger.warn(("spring listener init chain flow service! end!"));
        }
    }

    @Autowired
    public void setChainServiceManager(ChainServiceManager chainServiceManager) {
        this.chainServiceManager = chainServiceManager;
    }
}
