package com.blog.controller;


import com.blog.dto.BoardRequest;
import com.blog.dto.BoardResponse;
import com.blog.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Enumeration;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {


    private final BoardService boardService;
    @GetMapping("")
    public String BoardList() {

        return "board";
    }

    @GetMapping("/newpost")
    public String createBoardForm() {

        return "boardadd";
    }

    @PostMapping("/newpost")
    public String createBoard(BoardRequest boardRequest) {
        BoardResponse board=boardService.createBoard(boardRequest);
        if(board!=null){
            return "redirect:/index";
        }else{
            throw new RuntimeException("등록하려는 게시글의 이상발생");
        }
    }
}
