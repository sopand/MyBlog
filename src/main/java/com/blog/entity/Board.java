package com.blog.entity;


import com.blog.dto.BoardRequest;
import com.blog.dto.BoardResponse;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 게시글관련 Entity
 */
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

    @OneToMany(mappedBy = "board" ,cascade = CascadeType.ALL)
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


    /**
     * 게시글의 수정시 UPDATE를 해줄 더티체킹용 메서드.
     * 사진의 변경 여부에 따라서 썸네일 이미지 변경을 체크한다.
     * @param boardRequest
     */
    public void modifyBoardAndThubmnail(BoardRequest boardRequest){
        this.boardName=boardRequest.getBoardName();
        this.boardContent=boardRequest.getBoardContent();
        this.boardCategory=boardRequest.getBoardCategory();
        if(!boardRequest.getBoardThumbnail().equals("")){
            this.boardThumbnail=boardRequest.getBoardThumbnail();
        }
    }

    /**
     * 게시글의 조회수를 올리기 위한 더티체킹에 필요한 메서드
     */
    public void modifyBoardHit(){
        this.boardHit=boardHit+1;
    }

    /**
     * 게시글의 사진이 존재하지 않을경우 썸네일 컬럼의 값을 지우기 위한 메서드
     */
    public void isNullBoardThumnail(){
        this.boardThumbnail="";
    }


}
