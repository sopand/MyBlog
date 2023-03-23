package com.blog.dto;

import com.blog.entity.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardRequest {

    private Long boardId;
    private String boardName;
    private String boardContent;

    private String boardWriter;
    private String boardCategory;

    private String boardThumbnail;
    public Board toEntity(){
        return Board.builder().boardWriter(boardWriter).boardThumbnail(boardThumbnail).boardName(boardName).boardThumbnail(boardThumbnail).boardCategory(boardCategory).boardContent(boardContent).build();
    }




}
