package com.guce.zk;

import com.guce.manager.cache.NodeCacheZkClientWatcher;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("zkDemoService")
public class ZkDemoService extends NodeCacheZkClientWatcher {

    private static Logger logger = LoggerFactory.getLogger(ZkDemoService.class);

    @Value("${example.path}")
    private String zkPath;

    @Override
    public void nodeNotify(NodeCache nodeCache) {

        logger.info("path:{}",nodeCache.getCurrentData().getPath());
    }

    @Override
    public String getZkNodePath() {

        return zkPath;
    }

}
