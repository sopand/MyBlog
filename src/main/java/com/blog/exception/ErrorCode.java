package com.blog.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /**
     * 여러 에러에 대한 커스텀 설정을 해놓고 필요한 부분에서 사용시 에러가 발생하면 해당 에러에 맞는 메시지와 상태코드가 발생된다.
     * 이렇게 코드를 설정 해놓으면 에러코드를 좀 더 직관적으로 볼 수있고 에러에 대한 상세내용들을 사용자에게 보이지 않게 함으로써
     * 보안적으로 조금 더 좋은 방식 이라고 한다. ( ex : 기본 에러 메시지는 에러에 대한 모든 이유와 DB의 컬럼명까지 다보이게 되는데 이 방식은 에러 메시지만 출력된다 )
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"잘못된 요청입니다!!!"),
    UNAUTHORIZED_REQUEST(HttpStatus.UNAUTHORIZED,"인증되지 않은 사용자의 요청입니다!"),

    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN,"권한이 없는 사용자의 요청이에요!!"),

    NOT_FOUND(HttpStatus.NOT_FOUND,"리소스를 찾을수 없는 정보입니다..."),
    NOT_FOUND_REVIEW_NAME(HttpStatus.NOT_FOUND,"리뷰를 등록하려는 사람의 아이디값을 찾을수가 없습니다."),
    FAILED_CREATE_BOARD(HttpStatus.NOT_FOUND,"게시글 등록에 실패했습니다."),

    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED,"허용되지 않은 메소드에 대한 호출이에요!"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"내부 서버에 오류가 발생했습니다.. 관리자에게 문의 부탁드려요@!!!");

    private final HttpStatus httpStatus;
    private final String message;
}
