package com.blog.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Img {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name="img_id")
    private Long imgId;

    @Column(name="img_origin")
    private String imgOrigin;

    @Column(name="img_new")
    private String imgNew;


    @Column(name = "img_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date imgDate;


    @Builder
    public Img(Long imgId,String imgOrigin,String imgNew){
        this.imgId=imgId;
        this.imgOrigin=imgOrigin;
        this.imgNew=imgNew;
        this.imgDate=new Date();
    }

}
