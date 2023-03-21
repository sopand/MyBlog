package com.blog.entity;

import com.blog.dto.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review,Long> {




    @Query(value = "select new com.blog.dto.ReviewResponse(re) from Review re left join fetch re.board bo where re.board.boardId = :boardId GROUP BY re.reviewId",
            countQuery = "select count(re.reviewId) from Review re WHERE re.board = :boardId")
    Page<ReviewResponse> findReview(Long boardId, Pageable pageable);
}
