package com.guce.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by chengen.gu on 2018/10/17.
 */
@RestController
@RequestMapping("/flux")
public class FluxDemoController {
    private static Logger logger = LoggerFactory.getLogger(FluxDemoController.class);

    @GetMapping("/hello")
    public Mono<String> helloWorld(HttpServletRequest request , HttpServletResponse response){
        
        return Mono.just("Welcome to reactive world");
    }
}
