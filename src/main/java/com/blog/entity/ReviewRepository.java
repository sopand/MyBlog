package com.blog.entity;

import com.blog.dto.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {




    @Query(value = "select re from Review re left join fetch re.board bo where re.board.boardId = :boardId GROUP BY re.reviewId",
            countQuery = "select count(re.reviewId) from Review re WHERE re.board.boardId = :boardId")
    Page<Review> findReviewList(@Param("boardId")Long boardId, Pageable pageable);

     Review findByReviewId(Long reviewId);

    List<Review> findByReviewParent(Long reviewParent);


}
