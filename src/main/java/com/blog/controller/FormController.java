package com.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class FormController {

    @GetMapping("/index")
    public String IndexForm(Principal principal){

        return "index";
    }

}
