package com.blog.entity;

import com.blog.config.DataBaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(DataBaseConfig.class)
@ActiveProfiles("test")
class BoardRepositoryTest {

    @Autowired
    private BoardRepository repository;
    @Test
    void 전체_게시글찾기() {
        //given
        PageRequest pageRequest=PageRequest.of(0,5, Sort.by(Sort.DEFAULT_DIRECTION.DESC,"boardId"));
        //when
        Page<Board> board=repository.findAll(pageRequest);
        //then
        assertThat(board).isNotEmpty();
    }

    @Test
    void 게시글삭제(){
        //given
        List<Board> boardList=repository.findAll();
        Long boardId=boardList.get(0).getBoardId();
        //when
        repository.deleteByBoardId(boardId);

        //then
        Board board=repository.findByBoardId(boardId);
        assertThat(board).isNull();
    }

    @Test
    void 카테고리별_게시글찾기(){
        //given
        PageRequest pageRequest=PageRequest.of(0,5, Sort.by(Sort.DEFAULT_DIRECTION.DESC,"boardId"));
        String boardCategory="Project";
        //when
        Page<Board>boardList=repository.findByBoardCategory(boardCategory,pageRequest);
        //then
        assertThat(boardList).isNotEmpty();


    }

    @Test
    void 게시글_상세페이지(){
        //given
        List<Board> boardList=repository.findAll();
        Long boardId=boardList.get(0).getBoardId();
        //when
        Board board=repository.findByBoardId(boardId);
        //then
        assertThat(boardId).isEqualTo(board.getBoardId());
    }

    @Test
    void 게시글_검색하기(){
        //given
        String searchText="as";
        PageRequest pageRequest=PageRequest.of(0,5, Sort.by(Sort.DEFAULT_DIRECTION.DESC,"boardId"));
        //when
        Page<Board> getBoardList=repository.findSearchBoard(searchText,pageRequest);
        //then

        log.info("정보 확인 ->{}", getBoardList.getContent());
        assertThat(getBoardList.getContent()).isNotEmpty();
    }
}