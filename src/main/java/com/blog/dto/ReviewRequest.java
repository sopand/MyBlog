package com.blog.dto;

import com.blog.entity.Review;
import lombok.Data;

@Data
public class ReviewRequest {

    private String reviewName;
    private String reviewContent;


    public Review toEntity(){
        return Review.builder().reviewName(reviewName).reviewContent(reviewContent).build();

    }
}
