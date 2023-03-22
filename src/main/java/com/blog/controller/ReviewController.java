package com.blog.controller;

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

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @ResponseBody
    @PostMapping("/boards/review")
    public String createReview(ReviewRequest reviewRequest){
        reviewService.createReview(reviewRequest);
        return "리뷰 등록 완료";
    }
    @ResponseBody
    @GetMapping("/boards/review")
    public List<ReviewResponse> findReview(Long boardId,@PageableDefault(page = 0, size = 10, sort = "reviewId", direction = Sort.Direction.ASC) Pageable pageable){
        return reviewService.findReview(boardId,pageable);
    }
    @ResponseBody
    @DeleteMapping("/boards/review")
    public String deleteReview(Long reviewId){
        reviewService.deleteReview(reviewId);
        return "삭제 완료";
    }

}
