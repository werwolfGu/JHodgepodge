package com.guce.manager.cache;

import com.guce.manager.AbstractZkClient;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class NodeCacheZkClientWatcher extends AbstractZkClient {

    private static Logger logger = LoggerFactory.getLogger(NodeCacheZkClientWatcher.class);

    private NodeCache cache;

    @Override
    protected void initZkNodeStart() {

        try {
            cache = new NodeCache(zkClient,getZkNodePath());
            cache.start();
            this.addListener();
        } catch (Exception e) {
            logger.error("NodeCacheZkClientWatcher nodeCache start Exception:{}",e.getMessage(),e);
        }
    }

    @Override
    public void  addListener() throws Exception{

        NodeCacheListener listener = new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {

                nodeNotify(cache);
            }
        };
        cache.getListenable().addListener(listener);
    }

    /**
     * 当前节点发生改变时触发改方法  新增、修改、删除
     * @param nodeCache
     */
    public abstract void nodeNotify(NodeCache nodeCache);

}
