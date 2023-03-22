package com.blog.dto;

import com.blog.entity.Board;
import com.blog.entity.Review;
import lombok.Data;

@Data
public class ReviewRequest {

    private String reviewName;
    private String reviewContent;

    private Long reviewParent;
    private Long boardId;

    private Board board;

    public Review toEntity(){
        return Review.builder().board(board).reviewName(reviewName).reviewContent(reviewContent).build();

    }
}
