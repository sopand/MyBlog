package com.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FormController {

    @GetMapping("/index")
    public String IndexForm(){
        return "index";
    }

    @GetMapping("/users")
    public String LoginForm(){
        return "login";
    }

}
