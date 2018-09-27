package com.guce.controller;

import com.guce.service.DemoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/sync")
public class Demo {

    @Resource(name = "demoService")
    private DemoService demoService ;

    @RequestMapping(value = "/hello" ,method = {RequestMethod.GET,RequestMethod.POST})
    public String helloWorld() throws ExecutionException {

        return demoService.helloService("name","age");
    }
}
