package com.guce.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class ZkServiceDemo extends AbstractZkManager {

    private static Logger logger = LoggerFactory.getLogger(ZkServiceDemo.class);

    @Value("${example.path}")
    private String zkPath;

    @Override
    public String getZkPath() {
        return zkPath;
    }

    @Override
    public void zkPathAdded() {

    }

    @Override
    public void zkPathUpdated() {

    }

    @Override
    public void zkPathDeleted() {

    }


}
