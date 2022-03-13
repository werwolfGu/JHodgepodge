package com.guce.controller;

import com.gitee.starblues.integration.application.AutoPluginApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author chengen.gce
 * @DATE 2021/9/17 11:56 下午
 */
@RestController
public class PluginOperaterController {

    @Autowired
    private AutoPluginApplication pluginApplication;
    @RequestMapping(path = "/pluginOperater")
    public String unloadPlugin(String pluginId) {

        try {
            Boolean flag = pluginApplication.getPluginOperator().uninstall(pluginId,false);
            //Boolean flag = pluginApplication.getPluginOperator().stop(pluginId);
            return flag.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "yicahng";
    }
}
