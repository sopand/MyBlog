package com.blog.service;

import com.blog.dto.ReviewRequest;
import com.blog.entity.Board;
import com.blog.entity.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
