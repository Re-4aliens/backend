package com.aliens.backend.global.response;

import com.aliens.backend.global.response.success.SuccessCode;
import org.springframework.http.ResponseEntity;

public class SuccessResponse<T> extends ResponseEntity {
    public SuccessResponse(final SuccessCode successCode, final T result) {
        super(new CustomResponseDto<>(successCode.getCode(), result),
                successCode.getHttpStatus()
        );
    }

    public static <T> SuccessResponse<T> of(final SuccessCode successCode, final T result) {
        return new SuccessResponse<>(successCode, result);
    }

    public static SuccessResponse of(final SuccessCode successCode) {
        return new SuccessResponse(successCode, successCode.getMessage());
    }
}