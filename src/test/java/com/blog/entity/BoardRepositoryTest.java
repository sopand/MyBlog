package com.blog.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
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
        assertThat(board.getContent().get(0).getBoardId()).isEqualTo(1);
    }
}