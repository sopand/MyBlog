package com.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImgController {

    @ResponseBody
    @PostMapping("/boards/img")
    public String createBoardImg(){
        
        return "업로드";
    }
}
