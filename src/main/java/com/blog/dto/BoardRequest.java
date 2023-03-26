package com.blog.dto;

import com.blog.entity.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BoardRequest {

    private Long boardId;
    private String boardName;
    private String boardContent;

    private String boardWriter;

    private String boardCategory;



    private String boardThumbnail;

    private String boardSort;
    private String boardDirection;

    private String imgList;

    private String searchText;

    public Board toEntity(){
        return Board.builder().boardWriter(boardWriter).boardThumbnail(boardThumbnail).boardName(boardName).boardThumbnail(boardThumbnail).boardCategory(boardCategory).boardContent(boardContent).build();
    }




}
