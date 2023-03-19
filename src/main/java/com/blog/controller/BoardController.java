package com.blog.controller;


import com.blog.dto.BoardRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Enumeration;

@Controller
@RequestMapping("/boards")
public class BoardController {

    @GetMapping("")
    public String BoardList(){

        return "board";
    }

    @GetMapping("/newpost")
    public String createBoardForm(){

        return "boardadd";
    }

    @PostMapping("/newpost")
    public String createBoard(BoardRequest boardRequest, HttpServletRequest request){
        Enumeration params = request.getParameterNames();
        System.out.println("----------------------------");
        while (params.hasMoreElements()){
            String name = (String)params.nextElement();
            System.out.println(name + " : " +request.getParameter(name));
        }
        System.out.println("----------------------------");

        System.out.println(boardRequest);
        return "redirect:/index";
    }
}
