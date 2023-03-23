package com.blog.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"잘못된 요청입니다!!!"),
    UNAUTHORIZED_REQUEST(HttpStatus.UNAUTHORIZED,"인증되지 않은 사용자의 요청입니다!"),

    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN,"권한이 없는 사용자의 요청이에요!!"),

    NOT_FOUND(HttpStatus.NOT_FOUND,"리소스를 찾을수 없는 정보입니다..."),
    NOT_FOUND_REVIEW_NAME(HttpStatus.NOT_FOUND,"리뷰를 등록하려는 사람의 아이디값을 찾을수가 없습니다."),

    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED,"허용되지 않은 메소드에 대한 호출이에요!"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"내부 서버에 오류가 발생했습니다.. 관리자에게 문의 부탁드려요@!!!");

    private final HttpStatus httpStatus;
    private final String message;
}
