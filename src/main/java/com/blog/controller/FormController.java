package com.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FormController {

    /**
     * 메인페이지로 이동시켜주는 맵핑
     * @return
     */
    @GetMapping("/index")
    public String IndexForm(){
        return "index";
    }

    /**
     * 로그인 페이지로 이동시켜주는 맵핑
     * @return
     */
    @GetMapping("/users")
    public String LoginForm(){
        return "login";
    }

}
