package com.guce.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * https://www.cnblogs.com/TheoryDance/p/16246756.html
 * http://www.freemarker.net/#2
 * 如要通过 FreeMarker模板返回 ，就不能使用 @RestController 因为我们要控制页面的跳转
 * @Author chengen.gce
 * @DATE 2022/9/16 21:15
 */
@Slf4j
@Controller
public class FreeMarkerController {
    @GetMapping(value="/template")
    public String ftl(Model model ,String name ) throws ServletException, IOException {
        System.out.println("-------name:" + name);
        model.addAttribute("hello","hello world");

        return "template";
    }

    @GetMapping(value="/index")
    public String index(Model model ,String name) throws ServletException, IOException {

        System.out.println("-------name:" + name);
        model.addAttribute("userList", getData());
        model.addAttribute("dataList", getData());

        return "index";
    }

    public List<User> getData(){

        List<User> list = new ArrayList<>();
        for (int i = 0 ; i < 10 ; i++) {

            User user = new User();
            user.setAge(i + "");
            user.setName("张三");
            user.setEmail("88888888@qq.com");
            list.add(user);
        }
        return list;
    }
}
