package com.blog.service;

import com.blog.dto.BoardRequest;
import com.blog.dto.BoardResponse;
import com.blog.entity.Board;
import com.blog.entity.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public static Map<String,Object> findPaging(Page<Board> pagingBoards){
        Map<String, Object> pagingMap = new HashMap<>();
        int nowPage = pagingBoards.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, pagingBoards.getTotalPages());
        pagingMap.put("startPage", startPage);
        pagingMap.put("nowPage", nowPage);
        pagingMap.put("endPage", endPage);
        return pagingMap;
    }

    public Map<String,Object> findBoards(Pageable page){
        Page<Board> pagingBoards=boardRepository.findAll(page);
        List<BoardResponse> findBoards=pagingBoards.stream().map(BoardResponse::new).toList();
        Map<String, Object> pagingMap = findPaging(pagingBoards);
        pagingMap.put("findBoards",findBoards);
        return pagingMap;
    }
    public Map<String,Object> findBoardByCateogry(Pageable page , String boardCateogry){
        Page<Board> pagingBoards=boardRepository.findByBoardCategory(boardCateogry,page);
        List<BoardResponse> findBoards=pagingBoards.stream().map(BoardResponse::new).toList();
        Map<String, Object> pagingMap = findPaging(pagingBoards);
        pagingMap.put("findBoards",findBoards);
        return pagingMap;
    }


    @Transactional
    public BoardResponse createBoard(BoardRequest boardRequest){
        System.out.println(boardRequest.getBoardContent());
        Board board=boardRepository.save(boardRequest.toEntity());
        return new BoardResponse(board);
    }


    public BoardResponse findBoard(Long boardId){
        Board boardPS=boardRepository.findByBoardId(boardId);
        return new BoardResponse(boardPS);
    }

    @Transactional
    public int modifyBoardHit(Long boardId){
        return boardRepository.modifyBoardHit(boardId);

    }


}
