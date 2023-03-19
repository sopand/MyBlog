package com.blog.dto;

import com.blog.entity.Board;
import lombok.Getter;

import java.util.Date;

@Getter
public class BoardResponse {

    private String boardName;
    private int boardHit;
    private String boardContent;
    private Date boardDate;
    private Long boardId;

    private String boardCategory;

    private String boardThumbnail;


    public BoardResponse(Board entity){
        this.boardThumbnail=entity.getBoardThumbnail();
        this.boardName=entity.getBoardName();
        this.boardHit=entity.getBoardHit();
        this.boardContent=entity.getBoardContent();
        this.boardCategory=entity.getBoardCategory();
        this.boardId=entity.getBoardId();
        this.boardDate=entity.getBoardDate();
    }

}
