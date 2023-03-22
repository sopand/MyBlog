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
        Board boardId = Board.builder().boardId(reviewRequest.getBoardId()).build();
        reviewRequest.setBoard(boardId);
        reviewRepository.save(reviewRequest.toEntity());
    }

    public ReviewList findReview(Long boardId, Pageable pageable) {

        Page<Review> pagingReview = reviewRepository.findReview(boardId, pageable);
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
