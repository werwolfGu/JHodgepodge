package com.guce.manager.cache;

import com.guce.manager.AbstractZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TreeCacheZkClientWatcher extends AbstractZkClient {

    private static Logger logger = LoggerFactory.getLogger(TreeCacheZkClientWatcher.class);

    private TreeCache cache;

    protected void initZkNodeStart(){

        try {
            cache = TreeCache.newBuilder(zkClient, getZkNodePath()).setCacheData(true).build();
//            cache = new TreeCache(zkClient,getZkNodePath());
            cache.start();
        } catch (Exception e) {
            logger.error("TreeCacheZkClientWatcher start Exception:{}",e.getMessage(),e);
        }
    }

    public void addListener() throws Exception{

        TreeCacheListener listener = new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {

                nodeNotify(event);
            }
        };

    }

    public abstract void nodeNotify(TreeCacheEvent event);

}
