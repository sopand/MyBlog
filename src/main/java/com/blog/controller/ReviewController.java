package com.blog.controller;

import com.blog.dto.ReviewList;
import com.blog.dto.ReviewRequest;
import com.blog.dto.ReviewResponse;
import com.blog.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/boards/review")
    public String createReview(ReviewRequest reviewRequest){
        reviewService.createReview(reviewRequest);
        return "리뷰 등록 완료";
    }
    @GetMapping("/boards/review")
    public ReviewList findReview(Long boardId, @PageableDefault(page = 0, size = 7, sort = "reviewId", direction = Sort.Direction.ASC) Pageable pageable){
        return reviewService.findReviewList(boardId,pageable);
    }
    @DeleteMapping("/boards/review")
    public String deleteReview(Long reviewId){
        reviewService.deleteReview(reviewId);
        return "삭제 완료";
    }

    @GetMapping("/boards/review/parent")
    public List<ReviewResponse> findParentReview(Long reviewParent){
        return reviewService.findParentReview(reviewParent);
    }



}
