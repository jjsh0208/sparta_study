package com.sparta.myselectshop.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestApiException {
    private String errorMessage; // 에러 메시지
    private int statusCode; // 상태 코드
}