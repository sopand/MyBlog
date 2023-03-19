package com.blog.controller;

import com.blog.service.ImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ImgController {

    private final ImgService imgService;

    @PostMapping("/boards/img")
    public ModelAndView createBoardImg(MultipartHttpServletRequest request) throws  Exception{
        ModelAndView mv=imgService.createBoardImg(request.getFile("upload"));
        System.out.println(mv);
        return mv;
    }
}
