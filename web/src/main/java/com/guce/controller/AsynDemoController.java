package com.guce.controller;

import com.guce.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/async")
public class AsynDemoController {

    private static Logger logger = LoggerFactory.getLogger(AsynDemoController.class);
    @Resource(name = "demoService")
    private DemoService demoService ;

    @RequestMapping(value = "/hello")
    public CompletableFuture<String> asynHelloworld(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Thread.sleep(3000);

        return CompletableFuture.completedFuture(demoService.helloService("name2",null));

    }

    @RequestMapping(value = "/helloV1")
    public DeferredResult<String> asynHelloworld1(){
        DeferredResult<String> deferredResult = new DeferredResult<String>();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        deferredResult.setResult("hello world ->" + System.currentTimeMillis());

        return deferredResult;
    }
}
