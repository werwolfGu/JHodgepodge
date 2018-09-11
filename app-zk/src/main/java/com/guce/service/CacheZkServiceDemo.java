package com.guce.service;

import com.guce.manager.cache.NodeCacheZkClientWatcher;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class CacheZkServiceDemo extends NodeCacheZkClientWatcher {

    private static Logger logger = LoggerFactory.getLogger(CacheZkServiceDemo.class);

    @Value("${example.path}")
    private String zkPath;

    @Override
    public String getZkNodePath() {
        return zkPath;
    }

    @Override
    public void nodeNotify(NodeCache nodeCache) {

        logger.info("path:{}",nodeCache.getCurrentData().getPath());
    }
}
