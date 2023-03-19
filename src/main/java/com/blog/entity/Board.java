package com.blog.entity;


import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Board {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    @Id
    private Long boardId;

    @Column(name="board_name")
    private String boardName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "board_date")
    private Date boardDate;

    @Column(name = "board_hit")
    private int boardHit;

    @Column(name="board_category")
    private String boardCategory;

    @Column(name="board_content")
    private String boardContent;


    @Builder
    public Board(Long boardId,String boardName,int boardHit,String boardCategory,String boardContent){
        this.boardId=boardId;
        this.boardName=boardName;
        this.boardDate=new Date();
        this.boardHit=boardHit;
        this.boardCategory=boardCategory;
        this.boardContent=boardContent;
    }

}
