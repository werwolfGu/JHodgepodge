package com.guce.manager;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * NodeCache  监听当前节点的变化
 * PathChildrenCache 监听当前节点下的子节点的变化；
 * TreeCache 监听当前节点及子节点的变化
 *
 */
public abstract class AbstractZkClient implements InitializingBean,DisposableBean {

    private static Logger logger = LoggerFactory.getLogger(AbstractZkClient.class);

    @Autowired
    protected CuratorFramework zkClient;

    private static boolean zkClientStarted = false;



    public void afterPropertiesSet() throws Exception{

        logger.info("zookeeper client start...");
        if(!zkClientStarted){

            synchronized (AbstractZkClient.class){

                if(!zkClientStarted){

                    zkClient.getUnhandledErrorListenable().addListener((message, e) -> {
                        logger.error("zookeeper client UnhandledErrorListenable  Exception :{}",message,e);
                    });
                    zkClient.getConnectionStateListenable().addListener((c, newState) -> {
                        logger.info("zookeeper client ConnectionStateListenable new state :{}",newState);
                    });

                    zkClient.start();
                    zkClientStarted = true;
                }
            }
        }

        Assert.notNull(zkClient,"zkclient is null");
        Assert.notNull(getZkNodePath(),"zk node path is null");

        this.initZkNodeStart();
    }

    protected abstract void initZkNodeStart();

    public abstract String getZkNodePath();

    protected abstract void  addListener() throws Exception;

    @Override
    public void destroy() throws Exception{

        if(zkClient != null){
            zkClient.close();
        }
        logger.warn("zookeeper closed!");
    }


}
