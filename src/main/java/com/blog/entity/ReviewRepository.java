package com.blog.entity;

import com.blog.dto.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review,Long> {




    @Query(value = "select re from Review re left join fetch re.board bo where re.board.boardId = :boardId GROUP BY re.reviewId",
            countQuery = "select count(re.reviewId) from Review re WHERE re.board.boardId = :boardId")
    Page<Review> findReviewList(@Param("boardId")Long boardId, Pageable pageable);

     void deleteByReviewId(Long reviewId);

     Review findByReviewId(Long reviewId);

    @Modifying(clearAutomatically = true) // Update가 실행된후에 영속성 컨텍스트를 Clear, 지워버려서 기존의 Find한 객체의 값을 리셋 시킨다.
    @Query(value = "UPDATE Review re SET re.reviewGroupNo=re.reviewGroupNo+1 WHERE re.reviewId = :reviewId")
    int modifyReviewGroupNo(@Param("reviewId") Long reviewId);
}
