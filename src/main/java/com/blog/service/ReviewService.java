package com.blog.service;

import com.blog.dto.ReviewList;
import com.blog.dto.ReviewRequest;
import com.blog.dto.ReviewResponse;
import com.blog.entity.Board;
import com.blog.entity.Review;
import com.blog.entity.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public void createReview(ReviewRequest reviewRequest) {
        Board boardId = Board.builder().boardId(reviewRequest.getBoardId()).build();  // 댓글을 저장할 게시글의 고유번호
        reviewRequest.setBoard(boardId); // ReviewEntity로 변경하기위해 Board를 set
        Review review=null;
        if(reviewRequest.getReviewParent()!=null){
            review=reviewRepository.findByReviewId(reviewRequest.getReviewParent()); //review가 대댓글인지 댓글이지 여부를 확인하기 위함
        }
        if(review==null&&reviewRequest.getReviewParent()==null){ //null이라면 일반 댓글 등록
            reviewRepository.save(reviewRequest.noParent()); //Parent가 존재하지 않을때 Review Entity 변환 메서드
        }else { // 존재 한다면 Parent= 부모의 고유번호
            reviewRequest.setReviewDeep(review.getReviewDeep()+1); // 대댓글의 대댓글인지 그냥 대댓글인지 확인
            reviewRepository.save(reviewRequest.onParent()); // Parent가 존재할때의 Review Entity변환 메서드
            reviewRepository.modifyReviewGroupNo(review.getReviewId()); //저장시 부모의 대댓글 그룹의 숫자를 +1

        }

    }

    public ReviewList findReviewList(Long boardId, Pageable pageable) {

        Page<Review> pagingReview = reviewRepository.findReviewList(boardId, pageable);
        int nowPage = pagingReview.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, pagingReview.getTotalPages());
        List<ReviewResponse> review = pagingReview.getContent().stream().map(ReviewResponse::new).toList();
        ReviewList reviewList = ReviewList.builder().review(review).nowPage(nowPage).startPage(startPage).endPage(endPage).build();
        return reviewList;
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteByReviewId(reviewId);
    }
}
