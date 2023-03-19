package com.blog.dto;

import com.blog.entity.Board;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardRequest {

    private String boardName;
    private String boardContent;

    private String boardCategory;

    public Board toEntity(){
        return Board.builder().boardName(boardName).boardCategory(boardCategory).boardContent(boardContent).build();
    }




}
