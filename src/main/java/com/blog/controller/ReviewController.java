package com.blog.controller;

import com.blog.dto.PagingList;
import com.blog.dto.ReviewRequest;
import com.blog.dto.ReviewResponse;
import com.blog.exception.ErrorCode;
import com.blog.exception.CustomException;
import com.blog.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰를 등록시키는 기능을 담당하는 맵핑
     *
     * @param reviewRequest = 사용자가 입력한 리뷰의 데이터가 저장되어있는 객체
     * @return
     */
    @PostMapping("/boards/review")
    public String createReview(ReviewRequest reviewRequest) {
        System.out.println("요청값" + reviewRequest);
        if (reviewRequest.getReviewName() == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_REVIEW_NAME); // 입력한 데이터에 사용자의 이름이 없다면 로그인이 X 에러를 발생
        }
        reviewService.createReview(reviewRequest);
        return "리뷰 등록 완료";

    }

    /**
     * 게시글에 해당하는 리뷰 리스트를 찾아오는 맵핑  ( 페이징처리 )
     *
     * @param boardId  = 리뷰를 찾아올 게시글의 고유번호 PK
     * @param pageable = 페이징처리 데이터를 가지고 있는 객체
     * @return = 페이징전용 객체인 PagingList를 View작업을 위해서 리턴
     */
    @GetMapping("/boards/review/list")
    public PagingList findReview(Long boardId, @PageableDefault(page = 0, size = 7, sort = "reviewId", direction = Sort.Direction.ASC) Pageable pageable) {
        System.out.println("데이터체크");
        return reviewService.findReviewList(boardId, pageable);
    }

    /**
     * 리뷰 삭제기능을 담당하는 맵핑
     *
     * @param reviewId = 삭제할 리뷰의 고유번호 PK
     * @return
     */
    @DeleteMapping("/boards/review")
    public String deleteReview(Long reviewId) {
        reviewService.deleteReview(reviewId);
        return "삭제 완료";
    }

    /**
     * 리뷰의 하위에있는 대댓글들을 찾아오기 위한 맵핑
     *
     * @param reviewParent = 대댓글을 찾아올 상위 댓글의 고유번호 PK
     * @return = 찾아온 대댓글 리스트데이터를 리턴
     */
    @GetMapping("/boards/review/parent")
    public List<ReviewResponse> findParentReview(Long reviewParent) {
        return reviewService.findParentReview(reviewParent);
    }


    /**
     * 리뷰를 수정하기 위한 맵핑
     *
     * @param reviewRequest = 수정 할 데이터를 가지고 있는 객체, 해당 리뷰의 고유번호도 포함
     * @return
     */
    @PutMapping("/boards/review")
    public ReviewResponse modifyReviewContent(ReviewRequest reviewRequest) {
        return reviewService.modifyReviewContent(reviewRequest);
    }


}
