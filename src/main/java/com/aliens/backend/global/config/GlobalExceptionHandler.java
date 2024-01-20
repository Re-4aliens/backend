package com.aliens.backend.global.config;

import com.aliens.backend.global.error.CommonError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.lang.Integer.MAX_VALUE;

@RestControllerAdvice
@Order(value = MAX_VALUE)
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger("Global 에러 로그");

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> exception (Exception exception) {
        logger.info(exception.getMessage());

        return ResponseEntity
                .status(500)
                .body(
                        CommonError.INTERNAL_SERVER_ERROR.getDevelopCode()
                );
    }
}