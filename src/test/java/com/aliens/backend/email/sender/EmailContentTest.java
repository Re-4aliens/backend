package com.aliens.backend.email.sender;

import com.aliens.backend.email.service.EmailContent;
import com.aliens.backend.global.BaseIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class EmailContentTest extends BaseIntegrationTest {

    @Autowired
    EmailContent emailContent;

    @Test
    @DisplayName("이메일 인증 내용 가져오기")
    void getAuthenticationEmailContentTest() {
        //Given
        String token = "tmpToken";
        String domainURL = "tmp.com";

        //When
        String result = emailContent.getAuthenticationMailContent(token, domainURL);

        //Then
        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("이메일 인증 제목 가져오기")
    void getAuthenticationEmailTitleTest() {
        //When
        String result = emailContent.getAuthenticationMailTitle();

        //Then
        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("임시 비밀번호 발급 내용 가져오기")
    void getTemporaryPasswordContent() {
        //Given
        String tmpPassword = "tmpPassword";

        //When
        String result = emailContent.getTemporaryMailContent(tmpPassword);

        //Then
        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("임시 비밀번호 발급 제목 가져오기")
    void getTemporaryPasswordTitle() {
        //When
        String result = emailContent.getTemporaryMailTitle();

        //Then
        Assertions.assertNotNull(result);
    }
}
