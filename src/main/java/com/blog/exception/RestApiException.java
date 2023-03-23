package com.blog.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 에러코드 정의, 발생한 예외를 처리해줄 에러클래스를 추가.
// RuntimeException을 상속받아 언체크 예외로 활용한다.
@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException{
    private final ErrorCode errorCode;
}
