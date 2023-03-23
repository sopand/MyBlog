package com.blog.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter // 실제 사용자에게 JSON 형식으로 보여주기 위한 에러 응답형식을 지정.
public class ErrorResponse {
    private final LocalDateTime timestamp=LocalDateTime.now();
    private final int statusCode;
    private final String error;
    private final String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.statusCode = errorCode.getHttpStatus().value();
        this.error = errorCode.getHttpStatus().name();
        this.message = errorCode.getMessage();
    }
}
