package com.guce.spring.autoconfigurature;

import com.guce.chain.IChainService;
import com.guce.chain.anno.ChainService;
import com.guce.chain.manager.ChainServiceManager;
import com.guce.chain.model.ChainExecServiceWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * {@link org.springframework.beans.factory.config.BeanPostProcessor BeanPostProcessor}
 *
 * {@link org.springframework.beans.factory.config.BeanFactoryPostProcessor beanFactoryPostProcessor}
 * {@link org.springframework.context.annotation.ComponentScanBeanDefinitionParser beanFactoryPostProcessor}
 *
 *
 *
 * @Author chengen.gce
 * @DATE 2021/7/15 9:42 下午
 */
@Slf4j
//@Component("chainBeanPostProcessor")
@Deprecated
public class ChainBeanPostProcessor  implements BeanPostProcessor {

    /**
     * bean 有可能有代理类问题 {@link com.guce.annotation.ThreadPoolClientParser}
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof IChainService) {
            ChainService annoService = bean.getClass().getAnnotation(ChainService.class);
            if (annoService == null){
                log.warn("init chain service no annotation:{} ; service:{} ;有可能在配置文件中",beanName
                        ,bean.getClass().getCanonicalName());
                return bean ;
            }
            log.info("join chain node to flow :resource: {} ; className: {}" ,annoService.value() , beanName);
            IChainService service = (IChainService) bean;
            ChainExecServiceWrapper serviceWrapper = new ChainExecServiceWrapper();
            serviceWrapper.setChainService(service);
            serviceWrapper.annoParamWrapper(annoService);
            ChainServiceManager.chainNodeJoinFlow(serviceWrapper);

        }

        return bean;
    }
}
