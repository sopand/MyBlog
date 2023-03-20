package com.blog.dto;

import com.blog.entity.Review;
import lombok.Getter;

import java.util.Date;

@Getter
public class ReviewResponse {
    private Long reviewId;
    private Long reviewParent;
    private int reviewDeep;
    private Date reviewDate;
    private String reviewName;
    private String reviewContent;
    private int reviewGroupNo;

    public ReviewResponse(Review entity){
        this.reviewId=entity.getReviewId();
        this.reviewParent=entity.getReviewParent();
        this.reviewDeep=entity.getReviewDeep();
        this.reviewDate=entity.getReviewDate();
        this.reviewName=entity.getReviewName();
        this.reviewContent=entity.getReviewContent();
        this.reviewGroupNo=entity.getReviewGroupNo();
    }

}
