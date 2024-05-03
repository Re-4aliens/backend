package com.aliens.backend.email.controller.response;


public enum EmailResponse {

    DUPLICATED_EMAIL("사용 불가능한 이메일입니다."),
    AVAILABLE_EMAIL("사용 가능한 이메일입니다."),
    EMAIL_SEND_SUCCESS("이메일 전송 완료"),
    EMAIL_AUTHENTICATION_SUCCESS("이메일 인증 완료"),
    EMAIL_SEND_FAIL("이메일 전송 실패"),
    CAN_NEXT_STEP("이메일 인증이 완료되어 회원가입 가능"),
    CANT_NEXT_STEP("이메일 인증이 아직 되지 않았습니다.");

    private final String message;

    EmailResponse(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}