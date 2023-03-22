package com.blog.service;

import com.blog.dto.ReviewRequest;
import com.blog.dto.ReviewResponse;
import com.blog.entity.Board;
import com.blog.entity.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public void createReview(ReviewRequest reviewRequest){
        Board boardId=Board.builder().boardId(reviewRequest.getBoardId()).build();
        reviewRequest.setBoard(boardId);
        reviewRepository.save(reviewRequest.toEntity());
    }

    public List<ReviewResponse> findReview(Long boardId, Pageable pageable){
        List<ReviewResponse> review=reviewRepository.findReview(boardId,pageable).map(ReviewResponse::new).toList();

        return review;
    }

    @Transactional
    public void deleteReview(Long reviewId){
        reviewRepository.deleteByReviewId(reviewId);
    }
}
