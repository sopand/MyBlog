package com.blog.service;

import com.blog.dto.BoardResponse;
import com.blog.entity.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardService service;

    @Mock
    private BoardRepository repository;
    @Test
    void findBoards() {
        //given
        PageRequest pageRequest=PageRequest.of(0,5, Sort.by(Sort.DEFAULT_DIRECTION.DESC,"boardId"));
        //when
        Map<String,Object> boardMap=service.findAllBoards(pageRequest);
        //then
        List<BoardResponse> board= (List<BoardResponse>) boardMap.get("findBoards");
        assertThat(board).isNotNull();
    }

    @Test
    void createBoard() {
        //given

        //when

        //then
    }

    @Test
    void findBoard() {
    }

    @Test
    void modifyBoardHit(){
        //given
        Long boardId=3L;
        //when

        //then
    }
}