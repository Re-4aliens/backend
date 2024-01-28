package com.aliens.backend.global.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getDevelopCode();
    HttpStatus getHttpStatus();
    String getMessage();
}

