package com.grvyframework.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author guchengen495
 * @date 2020-01-20 15:19
 * @description
 */
@RestController
public class DemoController {

    @RequestMapping(value = "/test")
    public String test(HttpServletRequest request){

        return "hello grvy";
    }
}
