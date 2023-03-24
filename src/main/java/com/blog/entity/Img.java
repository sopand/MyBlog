package com.blog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity

public class Img {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_id")
    private Long imgId;

    @Column(name = "img_origin")
    private String imgOrigin;

    @Column(name = "img_new")
    private String imgNew;
    @Column(name="img_directory")
    private String imgDirectory;

    @Column(name = "img_date")
    private Date imgDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;


    @Builder
    public Img(String imgOrigin,String imgNew,String imgDirectory,Board board){
        this.imgOrigin=imgOrigin;
        this.imgNew=imgNew;
        this.imgDirectory=imgDirectory;
        this.imgDate=new Date();
        this.board=board;
    }

    public void modifyImgBoard(Board board){
        this.board=board;
    }



}
