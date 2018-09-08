package com.guce.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractZkManager implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(AbstractZkManager.class);

    @Autowired
    protected CuratorFramework zkClient;

    protected PathChildrenCache cache;

    private static boolean zkClientStarted = false;


    public void afterPropertiesSet() throws Exception{

        if(!zkClientStarted){

            synchronized (AbstractZkManager.class){

                if(!zkClientStarted){

                    zkClient.start();
                    zkClientStarted = true;
                }
            }
        }

        cache = new PathChildrenCache(zkClient,getZkPath(),true);
        cache.start();
        this.addListener();

    }


    public void  addListener() throws Exception {


        PathChildrenCacheListener listener = new PathChildrenCacheListener()
        {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception
            {
                switch ( event.getType() )
                {
                    case CHILD_ADDED:
                    {
                        zkPathAdded();
                        break;
                    }

                    case CHILD_UPDATED:
                    {
                        zkPathUpdated();
                        break;
                    }

                    case CHILD_REMOVED:
                    {
                        zkPathDeleted();
                        break;
                    }
                }
            }
        };
        cache.getListenable().addListener(listener);
    }

    public abstract String getZkPath();

    public abstract void zkPathAdded();
    public abstract void zkPathUpdated();
    public abstract void zkPathDeleted();

}
