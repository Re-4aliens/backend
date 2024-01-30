package com.aliens.backend.global.success;

import org.springframework.http.ResponseEntity;

public class SuccessResponseWithoutResult {
    private String code;
    private String message;

    public SuccessResponseWithoutResult(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public static ResponseEntity<SuccessResponseWithoutResult> toResponseEntity(SuccessCode successCode) {
        SuccessResponseWithoutResult res = new SuccessResponseWithoutResult(successCode.getCode(), successCode.getMessage());
        return new ResponseEntity<>(res, successCode.getHttpStatus());
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
