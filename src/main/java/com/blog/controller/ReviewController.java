package com.blog.controller;

import com.blog.dto.ReviewRequest;
import com.blog.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

}
