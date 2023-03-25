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
        if (reviewRequest.getReviewParent() != null) { //ReviewParent가 존재한다면 상위의 댓글이 있다는 뜻.
            Review review = reviewRepository.findByReviewId(reviewRequest.getReviewParent()); //상위에 있는 부모 댓글의 정보를 찾아옴
            reviewRequest.setReviewDeep(review.getReviewDeep() + 1); // 대댓글의 대댓글인지 그냥 대댓글인지 확인
            reviewRepository.save(reviewRequest.onParent()); // Parent가 존재할때의 Review Entity변환 메서드
            review.modifyReviewGroupNoUp(review.getReviewGroupNo());
        }else{
            reviewRepository.save(reviewRequest.unParent()); //Parent가 존재하지 않을때 Review Entity 변환 메서드
        }
    }
    @Transactional(readOnly = true)
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
        Review review = reviewRepository.findByReviewId(reviewId); //review가 대댓글인지 댓글인지 여부를 확인하기 위함
        if (review.getReviewParent() != null) { // 상위의 댓글이 존재하고 하위에는 없을 경우 ( 그냥 대댓글일 경우 )
            Review parentReview=reviewRepository.findByReviewId(review.getReviewParent());
            parentReview.modifyReviewGroupNoDown(review.getReviewGroupNo());
        }
        if (review.getReviewGroupNo() != 0) {
            reviewRepository.findByReviewParent(review.getReviewId()).forEach(entity -> reviewRepository.delete(entity));
        }
        reviewRepository.delete(review);
    }

    @Transactional
    public ReviewResponse modifyReviewContent(ReviewRequest reviewRequest){
        Review review=reviewRepository.findByReviewId(reviewRequest.getReviewId());
        review.modifyReview(reviewRequest.getReviewContent());
        return new ReviewResponse(review);
    }


    @Transactional(readOnly = true)
    public List<ReviewResponse> findParentReview(Long reviewParent){
        return reviewRepository.findByReviewParent(reviewParent).stream().map(ReviewResponse::new).toList();
    }

}
