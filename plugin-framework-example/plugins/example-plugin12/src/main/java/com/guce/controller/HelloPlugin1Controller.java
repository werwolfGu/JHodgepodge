package com.guce.controller;

import com.guce.service.IExamplePluginService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author chengen.gce
 * @DATE 2021/9/17 10:55 下午
 */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class HelloPlugin1Controller {

    @Autowired
    private IExamplePluginService examplePluginService;

    @RequestMapping(path = "/hello")
    public String getConfig(){
        String hello = examplePluginService.helloPlugin();
        log.warn("service: {}" , hello);
        return "hello plugin1 example";
    }
}
