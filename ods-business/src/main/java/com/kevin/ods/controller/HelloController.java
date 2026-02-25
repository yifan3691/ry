package com.kevin.ods.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/")
    public String hello(Model model) {
        // 向模板传递数据
        model.addAttribute("message", "Hello, Spring Boot with Thymeleaf!");
        return "index"; // 返回视图的名字，Thymeleaf 会去寻找 src/main/resources/templates/index.html
    }
}
