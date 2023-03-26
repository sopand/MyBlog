package com.blog.service;

import com.blog.dto.ReviewList;
import com.blog.dto.ReviewRequest;
import com.blog.dto.ReviewResponse;
import com.blog.entity.Board;
import com.blog.entity.Review;
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

    /**
     * 리뷰를 등록하는 기능을 담당하는 로직,
     * @param reviewRequest = DB에 Insert하려는 리뷰의 정보를 가지고있는 객체,
     *
     */
    @Transactional
    public void createReview(ReviewRequest reviewRequest) {
        if (reviewRequest.getReviewParent() != null) { //ReviewParent가 존재한다면 상위의 댓글이 있다는 뜻.
            Review getReviewEntity = reviewRepository.findByReviewId(reviewRequest.getReviewParent()); //상위에 있는 부모 댓글의 정보를 찾아옴
            reviewRequest.setReviewDeep(getReviewEntity.getReviewDeep() + 1); // 대댓글의 대댓글인지 그냥 대댓글인지 확인
            reviewRepository.save(reviewRequest.onParent()); // Parent가 존재할때의 Review Entity변환 메서드
            getReviewEntity.modifyReviewGroupNoUp(getReviewEntity.getReviewGroupNo());
        }else{
            reviewRepository.save(reviewRequest.unParent()); //Parent가 존재하지 않을때 Review Entity 변환 메서드
        }
    }

    /**
     * 작성한 리뷰들의 정보를 View에 출력해주기위한 데이터를 찾아오는 로직,
     * @param boardId = 리뷰를 출력해줄 게시판 글의 번호 ( 고유 번호 )를 받아오기 위함.
     * @param pageable = 페이징 처리에 필요한 데이터를 가지고 있는 객체
     * @return = 찾아온 데이터 데이터들을 저장하기 위한 객체, ReviewResponse객체와 뷰에 출력해줄 페이지의 갯수,현재 페이지정보 등을 가지고 있는 데이터를 함께 모아서 return 해준다.
     */
    @Transactional(readOnly = true)
    public ReviewList findReviewList(Long boardId, Pageable pageable) {
        Page<Review> getPagingReviewList = reviewRepository.findReviewList(boardId, pageable);
        int nowPage = getPagingReviewList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, getPagingReviewList.getTotalPages());
        List<ReviewResponse> review = getPagingReviewList.getContent().stream().map(ReviewResponse::new).toList();
        ReviewList reviewList = ReviewList.builder().review(review).nowPage(nowPage).startPage(startPage).endPage(endPage).build();
        return reviewList;
    }

    /**
     * 작성 되어있는 리뷰를 삭제하기 위한 로직,
     * @param reviewId = 삭제 처리하려고 하는 review의 고유번호 PK 값.
     */
    @Transactional
    public void deleteReview(Long reviewId) {
        Review getReviewById = reviewRepository.findByReviewId(reviewId); //review가 대댓글인지 댓글인지 여부를 확인하기 위함
        if (getReviewById.getReviewParent() != null) { // 상위의 댓글이 존재하고 하위에는 없을 경우 ( 그냥 대댓글일 경우 )
            Review getParentReview=reviewRepository.findByReviewId(getReviewById.getReviewParent());
            getParentReview.modifyReviewGroupNoDown(getReviewById.getReviewGroupNo());  // 상위 ParentReview의 그룹안에 속한 댓글의 수를 -1
        }
        if (getReviewById.getReviewGroupNo() != 0) {
            reviewRepository.findByReviewParent(getReviewById.getReviewId()).forEach(entity -> reviewRepository.delete(entity));
        }
        reviewRepository.delete(getReviewById);
    }

    /**
     * 리뷰를 수정하는 기능을 담당하는 로직,
     * @param reviewRequest = 수정하기 위해 작성된 리뷰의 데이터를 가지고 있는 객체,
     * @return = 더티체킹 방식으로 UPDATE를 진행하기 때문에, 성공 여부 체크를 위해 return
     */
    @Transactional
    public ReviewResponse modifyReviewContent(ReviewRequest reviewRequest){
        Review getReviewById=reviewRepository.findByReviewId(reviewRequest.getReviewId());
        getReviewById.modifyReview(reviewRequest.getReviewContent());
        return new ReviewResponse(getReviewById);
    }


    /**
     * 해당 로직은 댓글의 하위에 있는 대댓글들을 출력할때 사용되는 로직입니다, 상위 댓글의 더보기를 클릭시 해당 로직이 작동되어 하위의 대댓글을 출력,
     * @param reviewParent = 대댓글을 가지고있는 상위의 댓글의 고유번호 입니다.
     * @return // 상위 댓글이 가지고 있는 하위 댓글의 정보를 배열에 담아서 리턴해줍니다.
     */
    @Transactional(readOnly = true)
    public List<ReviewResponse> findParentReview(Long reviewParent){
        return reviewRepository.findByReviewParent(reviewParent).stream().map(ReviewResponse::new).toList();
    }

}
