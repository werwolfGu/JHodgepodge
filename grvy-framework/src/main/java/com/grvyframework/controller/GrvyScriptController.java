package com.grvyframework.controller;

import com.grvyframework.service.IGrvyScriptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author chengen.gu
 * @DATE 2020/2/14 3:43 下午
 */
@RestController
public class GrvyScriptController {

    private static Logger logger = LoggerFactory.getLogger(GrvyScriptController.class);

    @Autowired
    private IGrvyScriptService grvyScriptService;
    @RequestMapping(value = "/refreshGrvyScript")
    public String refreshGrvyScript(String key){

        return grvyScriptService.refreshGrvyScriptInfo(key);
    }


}
