package com.aliens.backend.global.success;

import org.springframework.http.ResponseEntity;

public class SuccessResponse<T> {

    private String code;
    private String message;
    private T result;

    public SuccessResponse(final String code, final String message, final T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }


    public static ResponseEntity<SuccessResponse> toResponseEntity(final SuccessCode successCode, final Object result) {
        SuccessResponse res = new SuccessResponse(successCode.getCode(), successCode.getMessage(), result);
        return new ResponseEntity<>(res, successCode.getHttpStatus());
    }
}
