package com.blog.entity;

import com.blog.config.DataBaseConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(DataBaseConfig.class)
class BoardRepositoryTest {

    @Autowired
    private BoardRepository repository;
    @Test
    void findAll() {
        //given
        PageRequest pageRequest=PageRequest.of(0,5, Sort.by(Sort.DEFAULT_DIRECTION.DESC,"boardId"));
        //when
        Page<Board> board=repository.findAll(pageRequest);
        //then
        assertThat(board).isNotNull();
    }

    @Test
    void modifyBoardHit(){
        //given
        Long boardId=3L;
        //when
        int chk=repository.modifyBoardHit(boardId);
        //then
        assertThat(chk).isEqualTo(1);
    }

    @Test
    void deleteByBoardId(){
        //given
        Long boardId=3L;
        //when
        repository.deleteByBoardId(boardId);
        Board board=repository.findByBoardId(boardId);
        //then
        assertThat(board).isNull();
    }
}