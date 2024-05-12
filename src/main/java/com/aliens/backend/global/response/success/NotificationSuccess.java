package com.aliens.backend.global.response.success;

import org.springframework.http.HttpStatus;

public enum NotificationSuccess implements SuccessCode {
    GET_NOTIFICATION_SUCCESS(HttpStatus.OK, "N001", "알림 정보 조회가 성공하였습니다."),
    REGISTER_TOKEN_SUCCESS(HttpStatus.OK, "N002", "FCM 토큰 등록이 완료되었습니다."),
    READ_NOTIFICATION_SUCCESS(HttpStatus.OK, "N003", "알림 읽음처리가 완료되었습니다."),
    GET_FCM_STATUS_SUCCESS(HttpStatus.OK, "N004", "알림 상태조회가 완료되었습니다."),
    CHANGE_FCM_STATUS_SUCCESS(HttpStatus.OK, "N005", "알림 상태변경이 완료되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    NotificationSuccess(final HttpStatus httpStatus, final String code, final String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
