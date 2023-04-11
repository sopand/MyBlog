package com.blog.controller;


import com.blog.dto.BoardRequest;
import com.blog.dto.BoardResponse;
import com.blog.dto.PagingList;
import com.blog.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {


    private final BoardService boardService;

    @GetMapping
    public String findBoardAll(Model model, @PageableDefault(page = 0, size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable) {
        PagingList getPagingResponse= boardService.findAllBoards(pageable);
        System.out.println("리스트 값"+getPagingResponse.getPagingList());
        model.addAttribute("getPagingResponse", getPagingResponse);
        return "board";
    }

    @GetMapping("/{boardCategory}")
    public String findBoardByCateogry(Model model, @PageableDefault(page = 0, size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable String boardCategory) {
        BoardRequest boardRequest=BoardRequest.builder().requestBoardCategory(boardCategory).build();
        PagingList getPagingResponse = boardService.findAllBoardByCateogrySearch(pageable,boardRequest);

        model.addAttribute("getPagingResponse", getPagingResponse);
        model.addAttribute("boardCategory", boardCategory);
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
    public String deleteBoard(Long boardId) {
        boardService.deleteBoard(boardId);
        return "redirect:/boards";
    }

    @GetMapping("/repost/{boardId}")
    public String modifyBoardForm(@PathVariable("boardId") Long boardId, Model model) {
        BoardResponse board = boardService.findBoard(boardId);
        model.addAttribute("board", board);
        return "boardmodify";
    }

    @PutMapping("/repost")
    public String modifyBoard(BoardRequest boardRequest) {
        boardService.modifyBoard(boardRequest);
        return "redirect:/boards";
    }

    @GetMapping("/search")
    public String findSearchBoard(Model model, BoardRequest boardRequest) {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.fromString(boardRequest.getBoardDirection()), boardRequest.getBoardSort()));
        PagingList getPagingResponse;
        if (boardRequest.getBoardCategory() != null) { // 카테고리가 선택되어 있는 경우,
            getPagingResponse = boardService.findAllBoardByCateogrySearch(pageRequest, boardRequest);
        } else {  // 카테고리 선택이 없는 경우
            getPagingResponse = boardService.findAllBoardByNoCategorySearch(pageRequest,boardRequest);
        }
        model.addAttribute("getPagingResponse", getPagingResponse);
        model.addAttribute("boardCategory", boardRequest.getBoardCategory());
        return "board";
    }


}
