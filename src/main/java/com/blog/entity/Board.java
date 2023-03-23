package com.blog.entity;


import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Board {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    @Id
    private Long boardId;

    @Column(name="board_name" , length = 40,nullable = false)
    private String boardName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "board_date")
    private Date boardDate;

    @Column(name = "board_hit")
    private int boardHit;

    @Column(name="board_category",length = 10,nullable = false)
    private String boardCategory;

    @Column(name="board_content",nullable = false,length = 1000)
    private String boardContent;

    @Column(name="board_thumbnail" ,length = 100)
    private String boardThumbnail;

    @Column(name="board_writer")
    private String boardWriter;

    @OneToMany(mappedBy = "board")
    private List<Review> review=new ArrayList<>();

    @Builder
    public Board(String boardWriter,Long boardId,String boardName,int boardHit,String boardCategory,String boardContent,String boardThumbnail){
        this.boardWriter=boardWriter;
        this.boardId=boardId;
        this.boardThumbnail=boardThumbnail;
        this.boardName=boardName;
        this.boardDate=new Date();
        this.boardHit=boardHit;
        this.boardCategory=boardCategory;
        this.boardContent=boardContent;
    }

    public void modifyBoard(String boardName,String boardContent,String boardCategory){
        this.boardName=boardName;
        this.boardContent=boardContent;
        this.boardCategory=boardCategory;
    }
    public void modifyBoardAndImg(String boardName,String boardContent,String boardCategory,String boardThumbnail){
        this.boardName=boardName;
        this.boardContent=boardContent;
        this.boardCategory=boardCategory;
        this.boardThumbnail=boardThumbnail;
    }
}
