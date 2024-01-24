package com.aliens.backend.email.sender;

import com.aliens.backend.email.service.EmailContent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailContentTest {

    @Autowired
    EmailContent emailContent;

    @Test
    @DisplayName("메일 내용 가져오기")
    void getContentTest() {
        //Given
        String token = "tmpToken";
        String domainURL = "tmp.com";

        //When
        String result = emailContent.getContent(token, domainURL);

        //Then
        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("메일 제목 가져오기")
    void getTitleTest() {
        //When
        String result = emailContent.getTitle();

        //Then
        Assertions.assertNotNull(result);
    }
}
