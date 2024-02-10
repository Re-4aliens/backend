package com.aliens.backend.global.success;

import org.springframework.http.HttpStatus;

public interface SuccessCode {
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
}
