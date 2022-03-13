package com.guce.listener;

import com.gitee.starblues.integration.listener.PluginListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author chengen.gce
 * @DATE 2021/9/17 11:28 下午
 */
@Slf4j
public class AppPluginListener implements PluginListener {
    @Override
    public void registry(String pluginId, boolean isStartInitial) {
        log.info("registry : {}" ,pluginId);
    }

    @Override
    public void unRegistry(String pluginId) {
        log.info("unRegistry : {}" ,pluginId);

    }

    @Override
    public void registryFailure(String pluginId, Throwable throwable) {
        log.info("registryFailure : {}" ,pluginId);

    }

    @Override
    public void unRegistryFailure(String pluginId, Throwable throwable) {
        log.info("unRegistryFailure : {}" ,pluginId);

    }
}
