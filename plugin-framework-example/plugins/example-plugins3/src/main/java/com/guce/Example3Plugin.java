package com.guce;

import com.gitee.starblues.annotation.ConfigDefinition;
import com.gitee.starblues.realize.BasePlugin;
import org.pf4j.PluginWrapper;

/**
 * @Author chengen.gce
 * @DATE 2021/9/21 9:02 上午
 */
@ConfigDefinition(fileName = "plugin3.yml" ,devSuffix = "-dev",prodSuffix = "-prod")
public class Example3Plugin  extends BasePlugin {

    public Example3Plugin(PluginWrapper wrapper) {
        super(wrapper);
    }
}
