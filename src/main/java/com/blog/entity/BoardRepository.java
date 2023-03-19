package com.blog.entity;

import com.blog.dto.BoardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board,Long> {

    Page<Board> findAll(Pageable pageable);

    Board findByBoardId(Long BoardId);
}
