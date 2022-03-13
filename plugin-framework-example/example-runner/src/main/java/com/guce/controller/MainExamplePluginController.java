package com.guce.controller;

import com.gitee.starblues.integration.user.PluginUser;
import com.guce.service.IExamplePluginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2021/9/18 9:31 上午
 */
@RestController
@Slf4j
public class MainExamplePluginController {


    @Resource(name="pluginUser")
    private PluginUser pluginUser;


    @RequestMapping(path = "/helloV1")
    public String getConfig(){
        //String hello = examplePluginService.helloPlugin();
        List<IExamplePluginService > pluginBeans =
        pluginUser.getPluginBeans("example-plugin12",IExamplePluginService.class);
        String hello = pluginBeans.get(0).helloPlugin();
        log.warn("service: {}" , hello);

        pluginBeans =
                pluginUser.getPluginBeans("example-plugin3",IExamplePluginService.class);

        String hello3 = pluginBeans.get(0).helloPlugin();
        log.warn("plugins 3 : {}" , hello3);

        return hello + hello3;
    }
}
