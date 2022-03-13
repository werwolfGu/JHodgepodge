package com.guce.listener;

import lombok.extern.slf4j.Slf4j;
import org.pf4j.PluginState;
import org.pf4j.PluginStateEvent;
import org.pf4j.PluginStateListener;

/**
 * pf4j 插件监听器
 * @author starBlues
 * @version 1.0
 * @since 2021-07-24
 */
@Slf4j
public class Pf4jPluginListener implements PluginStateListener {
    @Override
    public void pluginStateChanged(PluginStateEvent event) {
        PluginState pluginState = event.getPluginState();
        log.info("Plugin {} {}", event.getPlugin().getPluginId(), pluginState.toString());
    }
}
