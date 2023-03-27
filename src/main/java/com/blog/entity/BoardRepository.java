package com.blog.entity;

import com.blog.dto.BoardRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board,Long> {

    Page<Board> findAll(Pageable pageable);

    @Query( value = "select bo from Board bo WHERE ( bo.boardCategory = :#{#dto.boardCategory} and bo.boardContent LIKE %:#{#dto.searchText}% )or ( bo.boardCategory = :#{#dto.boardCategory} and bo.boardName LIKE %:#{#dto.searchText}% )",
            countQuery = "select count(bo.boardId) from Board bo WHERE ( bo.boardCategory = :#{#dto.boardCategory} and bo.boardContent LIKE %:#{#dto.searchText}% )or ( bo.boardCategory = :#{#dto.boardCategory} and bo.boardName LIKE %:#{#dto.searchText}% )")
    Page<Board> findSearchBoard(@Param("dto")BoardRequest dto, Pageable pageable);

    @Query( value = "select bo from Board bo WHERE bo.boardContent LIKE %:#{#dto.searchText}% or bo.boardName LIKE %:#{#dto.searchText}%",
            countQuery = "select count(bo.boardId) from Board bo WHERE bo.boardContent LIKE %:#{#dto.searchText}% or bo.boardName LIKE %:#{#dto.searchText}%")
    Page<Board> findSearchBoardNoBoardCateogry(@Param("dto")BoardRequest dto, Pageable pageable);




    Board findByBoardId(Long boardId);

    Page<Board> findByBoardCategory(String boardCategory,Pageable pageable);


    void deleteByBoardId(Long boardId);



}
