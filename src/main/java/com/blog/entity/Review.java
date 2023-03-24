package com.blog.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "review_parent")
    private Long reviewParent;

    @Column(name = "review_deep")
    private int reviewDeep;

    @Column(name = "review_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewDate;

    @Column(name = "review_name")
    private String reviewName;

    @Column(name = "review_content")
    private String reviewContent;

    @Column(name = "review_group_no")
    private int reviewGroupNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Review(Board board, Long reviewId, Long reviewParent, int reviewDeep, String reviewName, String reviewContent, int reviewGroupNo) {
        this.board = board;
        this.reviewId = reviewId;
        this.reviewParent = reviewParent;
        this.reviewName = reviewName;
        this.reviewDate = new Date();
        this.reviewContent = reviewContent;
        this.reviewGroupNo = reviewGroupNo;
        this.reviewDeep = reviewDeep;

    }

    public void modifyReview(String reviewContent){
        this.reviewContent=reviewContent;
    }


}
