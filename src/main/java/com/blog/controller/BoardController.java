package com.blog.controller;


import com.blog.dto.BoardRequest;
import com.blog.dto.BoardResponse;
import com.blog.dto.PagingList;
import com.blog.exception.CustomException;
import com.blog.exception.ErrorCode;
import com.blog.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {


    private final BoardService boardService;

    /**
     * 전체 게시글 조회시 사용하는 맵핑
     *
     * @param model    = View에 출력해줄 데이터를 설정해주는 객체
     * @param pageable = 페이징 처리를 위한 기본 데이터가 설정되어있는 객체
     * @return = 로직의 처리가 끝나면 board.html로이동
     */
    @GetMapping
    public String findBoardAll(Model model, @PageableDefault(page = 0, size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable) {
        PagingList getPagingResponse = boardService.findAllBoards(pageable);   // 페이징 응답을 위해 만든 PagingList객체를 비즈니스 로직에서 리턴받는다
        model.addAttribute("getPagingResponse", getPagingResponse);
        return "board";
    }

    /**
     * 게시물의 카테고리별 조회시 사용하는 맵핑
     *
     * @param model         = View에 출력해줄 데이터를 설정해주는 객체
     * @param pageable      = 페이징 처리를 위한 기본 데이터가 설정되어있는 객체
     * @param boardCategory = 사용자가 선택한 카테고리의 값
     * @return 로직이 끝나면 board.html로 이동
     */
    @GetMapping("/menu/{boardCategory}")
    public String findBoardByCateogry(Model model, @PageableDefault(page = 0, size = 10, sort = "boardId", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable String boardCategory) {
        BoardRequest boardRequest = BoardRequest.builder().requestBoardCategory(boardCategory).build(); //카테고리 조회,카테고리에서 검색을 서비스 1개로 사용하기 위해  카테고리를 reqeust객체에 담음
        PagingList getPagingResponse = boardService.findAllBoardByCateogrySearch(pageable, boardRequest);  //

        model.addAttribute("getPagingResponse", getPagingResponse);
        model.addAttribute("boardCategory", boardCategory);
        return "board";
    }


    /**
     * 게시글의 상세보기 출력을 위한 맵핑
     *
     * @param model   = View에 출력해줄 데이터를 설정해주는 객체
     * @param boardId = 사용자가 선택한 게시글의 고유번호 PK
     * @return = boarddetail.html로 이동
     */

    @GetMapping("/detail/{boardId}")
    public String findBoard(Model model, @PathVariable Long boardId) {
        BoardResponse board = boardService.findBoard(boardId);
        model.addAttribute("board", board);
        return "boarddetail";
    }

    /**
     * 게시글을 생성하기위한 Form 페이지로 이동하는 맵핑
     *
     * @return = boardadd.html 이동
     */
    @GetMapping("/newpost")
    public String createBoardForm() {
        return "boardadd";
    }

    /**
     * 게시글을 생성하는 기능을 담당하는 맵핑
     * @param boardRequest = 게시글을 등록하기 위해 입력한 데이터들이 저장되어있는 객체
     * @return = 생성기능은 forward로 이동 시 새로고침으로 인한 중복데이터 생성의 문제가 발생할 수 있어 redirect로 index 페이지 이동
     */
    @PostMapping("/newpost")
    public String createBoard(BoardRequest boardRequest) {
        BoardResponse board = boardService.createBoard(boardRequest);
        if (board == null) {  //
            throw new CustomException(ErrorCode.FAILED_CREATE_BOARD);
        }
        return "redirect:/index";


    }

    /**
     * 게시글의 삭제기능을 담당하는 맵핑
     * @param boardId = 삭제하려고 하는 게시글의 고유번호 PK
     * @return = 생성과 마찬가지로 삭제도 맵핑의 중복작동을 막기위해 redirect
     */
    @DeleteMapping
    public String deleteBoard(Long boardId) {
        boardService.deleteBoard(boardId);
        return "redirect:/boards";
    }

    /**
     * 게시글을 수정하기위한 수정 페이지 ( 수정 폼 ) 으로 이동하는 맵핑
     * @param boardId = 수정할 게시글의 고유번호 PK
     * @param model = View에 출력할 데이터를 설정해주는 객체
     * @return
     */
    @GetMapping("/repost/{boardId}")
    public String modifyBoardForm(@PathVariable("boardId") Long boardId, Model model) {
        BoardResponse board = boardService.findBoard(boardId);
        model.addAttribute("board", board);
        return "boardmodify";
    }

    /**
     * 게시글의 수정 기능을 담당하는 맵핑
     * @param boardRequest = 수정하기위해 입력한 데이터들을 저장하고 있는 객체
     * @return
     */
    @PutMapping("/repost")
    public String modifyBoard(BoardRequest boardRequest) {
        boardService.modifyBoard(boardRequest);
        return "redirect:/boards";
    }

    /**
     * 게시글에서 검색을 할 경우 사용하는 맵핑
     * @param model = View에 출력할 데이터를 설정하는 객체
     * @param boardRequest = 검색어
     * @return
     */
    @GetMapping("/search")
    public String findSearchBoard(Model model, BoardRequest boardRequest) {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.fromString(boardRequest.getBoardDirection()), boardRequest.getBoardSort()));
        PagingList getPagingResponse;
        if (boardRequest.getBoardCategory() != null) { // 카테고리가 선택되어 있는 경우,
            getPagingResponse = boardService.findAllBoardByCateogrySearch(pageRequest, boardRequest);
        } else {  // 카테고리 선택이 없는 경우
            getPagingResponse = boardService.findAllBoardByNoCategorySearch(pageRequest, boardRequest);
        }
        model.addAttribute("getPagingResponse", getPagingResponse);
        model.addAttribute("boardCategory", boardRequest.getBoardCategory());
        return "board";
    }


}
