package com.blog.dto;
import com.blog.entity.Review;
import lombok.Getter;

import java.util.Date;


/**
 * 리뷰의 응답데이터를 보내기 위한 객체
 */
@Getter
public class ReviewResponse {
    private Long reviewId;
    private Long reviewParent;
    private int reviewDeep;
    private Date reviewDate;
    private String reviewName;
    private String reviewContent;
    private int reviewGroupNo;




    private Long boardId;


    public ReviewResponse(Review entity){
        this.reviewId=entity.getReviewId();
        this.reviewParent=entity.getReviewParent();
        this.reviewDeep=entity.getReviewDeep();
        this.reviewDate=entity.getReviewDate();
        this.reviewName=entity.getReviewName();
        this.reviewContent=entity.getReviewContent();
        this.reviewGroupNo=entity.getReviewGroupNo();
        this.boardId=entity.getBoard().getBoardId();
    }


}
