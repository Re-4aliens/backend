package com.aliens.backend.member.controller.dto.response;

public enum MemberResponse {
    ALREADY_SIGN_UP("이미 회원 가입되어 있습니다."),
    SIGN_UP_SUCCESS("회원가입이 완료되었습니다."),
    WITHDRAW_SUCCESS("회원 탈퇴되었습니다."),
    TEMPORARY_PASSWORD_GENERATED_SUCCESS("임시 비밀번호가 발급되었습니다. 이메일을 확인해주세요."),
    PASSWORD_CHANGE_SUCCESS("비밀번호 변경이 완료되었습니다."),
    PROFILE_IMAGE_CHANGE_SUCCESS("프로필 이미지 변경이 완료되었습니다."),
    ABOUT_ME_CHANGE_SUCCESS("자기소개 변경이 완료되었습니다."),
    MBTI_CHANGE_SUCCESS("MBTI 변경이 완료되었습니다.");

    private final String message;

    MemberResponse(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}