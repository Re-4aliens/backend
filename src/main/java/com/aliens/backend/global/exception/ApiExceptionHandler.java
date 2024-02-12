package com.aliens.backend.global.exception;

import com.aliens.backend.global.response.error.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(value = Integer.MIN_VALUE)
public class ApiExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger("RestApi 에러 로그");

    @ExceptionHandler(value = RestApiException.class)
    public ResponseEntity<Object> apiException(RestApiException apiException) {
        ErrorCode errorCode = apiException.getErrorCode();
        logger.info(errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(errorCode.getDevelopCode());
    }
}