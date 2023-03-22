package com.blog.dto;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ReviewList {

    private List<ReviewResponse> review;
    private int nowPage;
    private int endPage;
    private int startPage;





}
