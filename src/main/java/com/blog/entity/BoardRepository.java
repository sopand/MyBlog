package com.blog.entity;

import com.blog.dto.BoardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board,Long> {

    Page<Board> findAll(Pageable pageable);

    Board findByBoardId(Long boardId);

    Page<Board> findByBoardCategory (String boardCategory,Pageable pageable);


    @Modifying(clearAutomatically = true) // Update가 실행된후에 영속성 컨텍스트를 Clear, 지워버려서 기존의 Find한 객체의 값을 리셋 시킨다.
    @Query(value = "UPDATE Board b SET b.boardHit = b.boardHit+1 WHERE b.boardId = :boardId")
    int modifyBoardHit(Long boardId);

    void deleteByBoardId(Long boardId);
}
