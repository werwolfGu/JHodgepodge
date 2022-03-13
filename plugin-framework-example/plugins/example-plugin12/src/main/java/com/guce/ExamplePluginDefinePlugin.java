package com.guce;

import com.gitee.starblues.annotation.ConfigDefinition;
import com.gitee.starblues.realize.BasePlugin;
import org.pf4j.PluginWrapper;

/**
 * @Author chengen.gce
 * @DATE 2021/9/17 10:53 下午
 */
@ConfigDefinition(fileName = "plugin1.yml", devSuffix = "-dev", prodSuffix = "-prod")
public class ExamplePluginDefinePlugin extends BasePlugin {

    public ExamplePluginDefinePlugin(PluginWrapper wrapper) {
        super(wrapper);
    }
}
