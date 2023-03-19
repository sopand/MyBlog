package com.blog.service;

import com.blog.dto.BoardRequest;
import com.blog.dto.BoardResponse;
import com.blog.entity.Board;
import com.blog.entity.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardResponse createBoard(BoardRequest boardRequest){
        System.out.println(boardRequest.getBoardContent());
        Board board=boardRepository.save(boardRequest.toEntity());
        return new BoardResponse(board);
    }


}
