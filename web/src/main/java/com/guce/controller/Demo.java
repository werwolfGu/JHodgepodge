package com.guce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sync")
public class Demo {

    /*@Resource(name = "demoService")
    private DemoService demoService ;

    @RequestMapping(value = "/hello" ,method = {RequestMethod.GET,RequestMethod.POST})
    public String helloWorld() throws ExecutionException {

        return demoService.helloService("name","age");
    }*/
}
