package com.blog.dto;

import com.blog.entity.Board;
import com.blog.entity.Review;
import lombok.Data;

@Data
public class ReviewRequest {

    private String reviewName;
    private String reviewContent;

    private Long reviewParent;

    private Long reviewId;
    private int reviewDeep;
    private Long boardId;

    private Board board;

    public Review onParent(){
        return Review.builder().reviewDeep(reviewDeep).reviewParent(reviewParent).board(board).reviewName(reviewName).reviewContent(reviewContent).build();
    }
    public Review noParent(){
        return Review.builder().board(board).reviewName(reviewName).reviewContent(reviewContent).build();
    }
}
