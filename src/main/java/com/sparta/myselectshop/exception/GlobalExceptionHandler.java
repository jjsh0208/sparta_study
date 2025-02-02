package com.sparta.myselectshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 모든 컨트롤러를 공통적으로 처리하기위한 어노테이션
// 모든 컨트롤러에서 발생하는 예외처리를 잡아올 수 있음
// 예외처리를 중앙집중화 하기가 좋다
// 하나의 클래스로 모든 예외처리를 처리할 수 있다.
// 모듈화 하여 관리(유지보수) 하기가 좋다. -> 개발의 향상성이 좋아짐
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Exception 해당 예외처리 발생을 처리하는 메서드
    // 컨트롤러에서 발생한 예외처리를 처리하기 위한 어노테이션
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<RestApiException> handleException(IllegalArgumentException ex) {
        System.out.println("FolderController.handleException");
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                // HTTP body
                restApiException,
                // HTTP status code
                HttpStatus.BAD_REQUEST
        );
    }

}
