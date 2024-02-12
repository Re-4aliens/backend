package com.aliens.backend.global.response.success;

import org.springframework.http.HttpStatus;

public enum MemberSuccess implements SuccessCode {
    SIGN_UP_SUCCESS(HttpStatus.OK, "M001", "회원가입이 완료되었습니다."),
    WITHDRAW_SUCCESS(HttpStatus.OK, "M002", "회원 탈퇴되었습니다."),
    TEMPORARY_PASSWORD_GENERATED_SUCCESS(HttpStatus.OK, "M003", "임시 비밀번호가 발급되었습니다. 이메일을 확인해주세요."),
    PASSWORD_CHANGE_SUCCESS(HttpStatus.OK, "M004", "비밀번호 변경이 완료되었습니다."),
    PROFILE_IMAGE_CHANGE_SUCCESS(HttpStatus.OK, "M005", "프로필 이미지 변경이 완료되었습니다."),
    ABOUT_ME_CHANGE_SUCCESS(HttpStatus.OK, "M006", "자기소개 변경이 완료되었습니다."),
    MBTI_CHANGE_SUCCESS(HttpStatus.OK, "M007", "MBTI 변경이 완료되었습니다."),
    GET_MEMBER_MATCHING_STATUS_SUCCESS(HttpStatus.OK, "M008", "회원 상태 조회를 완료했습니다."),
    GET_MEMBER_PAGE_SUCCESS(HttpStatus.OK, "M009", "회원 페이지 조회를 완료했습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    MemberSuccess(final HttpStatus httpStatus, final String code, final String message) {
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
