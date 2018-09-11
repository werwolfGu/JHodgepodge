package com.guce.manager.cache;

import com.guce.manager.AbstractZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PathChildrenCacheZkClientWatcher extends AbstractZkClient {

    private static Logger logger = LoggerFactory.getLogger(PathChildrenCacheZkClientWatcher.class);
    protected PathChildrenCache cache;

    @Override
    protected void initZkNodeStart(){

        try {
            cache = new PathChildrenCache(zkClient,getZkNodePath(),true);
            cache.start();
            this.addListener();

        } catch (Exception e) {
            logger.error("PathChildrenCacheZkClientWatcher start Exception:{}",e.getMessage(),e);
        }
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
                        zkChildrenPathAdded(client,event);
                        break;
                    }

                    case CHILD_UPDATED:
                    {
                        zkChildrenPathUpdated(client,event);
                        break;
                    }

                    case CHILD_REMOVED:
                    {
                        zkChildrenPathDeleted(client,event);
                        break;
                    }
                }
            }
        };
        cache.getListenable().addListener(listener);
    }


    /**
     * 新增子节点 触发该方法
     * @param client
     * @param event
     */
    public abstract void zkChildrenPathAdded(CuratorFramework client, PathChildrenCacheEvent event);

    /**
     * 修改子节点触发改方法
     * @param client
     * @param event
     */
    public abstract void zkChildrenPathUpdated(CuratorFramework client, PathChildrenCacheEvent event);

    /**
     * 删除子节点触发该方法
     * @param client
     * @param event
     */
    public abstract void zkChildrenPathDeleted(CuratorFramework client, PathChildrenCacheEvent event);
}
