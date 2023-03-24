package com.blog.controller;


import com.blog.dto.BoardRequest;
import com.blog.dto.BoardResponse;
import com.blog.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Enumeration;
import java.util.Map;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {


    private final BoardService boardService;

    @GetMapping("")
    public String findBoardAll(Model model, @PageableDefault(page = 0, size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable) {
        Map<String, Object> boardMap = boardService.findBoardAll(pageable);
        model.addAttribute("boardMap", boardMap);
        return "board";
    }

    @GetMapping("/{boardCategory}")
    public String findBoardByCateogry(Model model, @PageableDefault(page = 0, size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable String boardCategory) {
        Map<String, Object> boardMap = boardService.findBoardByCateogry(pageable, boardCategory);
        model.addAttribute("boardMap", boardMap);
        model.addAttribute("boardCategory",boardCategory);
        return "board";
    }

    @GetMapping("/detail/{boardId}")
    public String findBoard(Model model, @PathVariable Long boardId) {
        BoardResponse board = boardService.findBoard(boardId);
        model.addAttribute("board", board);
        return "boarddetail";
    }

    @GetMapping("/newpost")
    public String createBoardForm() {
        return "boardadd";
    }

    @PostMapping("/newpost")
    public String createBoard(BoardRequest boardRequest) {

        BoardResponse board = boardService.createBoard(boardRequest);
        if (board != null) {
            return "redirect:/index";
        } else {
            throw new IllegalStateException("게시글 등록 실패");
        }
    }

    @DeleteMapping
    public String deleteBoard(Long boardId){
        boardService.deleteBoard(boardId);
        return "redirect:/boards";
    }

    @GetMapping("/repost/{boardId}")
    public String modifyBoardForm(@PathVariable("boardId")Long boardId,Model model){
        BoardResponse board=boardService.findBoard(boardId);
        model.addAttribute("board",board);
        return "boardmodify";
    }

    @PutMapping("/repost")
    public String modifyBoard(BoardRequest boardRequest){
        boardService.modifyBoard(boardRequest);
        return "redirect:/boards";
    }
}
