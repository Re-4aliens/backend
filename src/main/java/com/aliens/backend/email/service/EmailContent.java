package com.aliens.backend.email.service;

import org.springframework.stereotype.Component;

@Component
public class EmailContent {

    public String getAuthenticationMailTitle() {
        return "[FriendShip] Account Verification 회원가입 이메일 인증";
    }

    public String getAuthenticationMailContent(String token, String domainUrl) {
        String englishContent = EmailContentType.AUTHENTICATION_ENGLISH.getContent()
                .replace("{DOMAIN}", domainUrl)
                .replace("{TOKEN}", token);

        String koreanContent = EmailContentType.AUTHENTICATION_KOREAN.getContent()
                .replace("{DOMAIN}", domainUrl)
                .replace("{TOKEN}", token);

        return englishContent + koreanContent;
    }

    public String getTemporaryMailTitle() {
        return "[FriendShip] 임시 비밀번호 발급";
    }

    public String getTemporaryMailContent(final String tmpPassword) {
        String englishContent = EmailContentType.TEMPORARY_ENGLISH.getContent()
                .replace("{TMP_PASSWORD}", tmpPassword);

        String koreanContent = EmailContentType.TEMPORARY_KOREAN.getContent()
                .replace("{TMP_PASSWORD}", tmpPassword);

        return englishContent + "\n\n" + koreanContent;
    }
}
