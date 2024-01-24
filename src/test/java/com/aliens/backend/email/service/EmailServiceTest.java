package com.aliens.backend.email.service;

import com.aliens.backend.email.controller.response.EmailResponse;
import com.aliens.backend.email.domain.EmailAuthentication;
import com.aliens.backend.email.domain.repository.EmailAuthenticationRepository;
import com.aliens.backend.global.encode.SymmetricKeyEncoder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    EmailService emailService;
    @Autowired
    SymmetricKeyEncoder symmetricKeyEncoder;
    @Autowired
    EmailAuthenticationRepository emailAuthenticationRepository;

    String givenEmail;
    EmailAuthentication emailEntity;

    @BeforeEach
    void setUp() {
        givenEmail = "tmp@example.com";
        emailEntity = new EmailAuthentication(givenEmail);
        emailAuthenticationRepository.save(emailEntity);
    }

    @AfterEach
    void afterDown() {
        emailAuthenticationRepository.deleteAll();
    }

    @Test
    @DisplayName("이메일 중복 검사 - 사용 불가한 이메일")
    void duplicateCheckTestNotAvailable() {
        //When
        String result = emailService.duplicateCheck(givenEmail);

        //Then
        Assertions.assertEquals(EmailResponse.DUPLICATED_EMAIL.getMessage(), result);
    }

    @Test
    @DisplayName("이메일 중복 검사 - 사용 가능한 이메일")
    void duplicateCheckTestAvailable() {
        //Given
        String newEmail = "newEmail@example.com";

        //When
        String result = emailService.duplicateCheck(newEmail);

        //Then
        Assertions.assertEquals(EmailResponse.AVAILABLE_EMAIL.getMessage(), result);
    }

    @Test
    @DisplayName("검증 이메일 전송")
    void sendAuthenticationEmailTest() throws Exception {
        //Given
        String newEmail = "newEmail@example.com";

        //When
        String result = emailService.sendAuthenticationEmail(newEmail);

        //Then
        Assertions.assertEquals(EmailResponse.EMAIL_SEND_SUCCESS.getMessage(), result);
    }

    @Test
    @DisplayName("이메일 검증")
    void authenticateEmailTest() throws Exception {
        //Given
        Long entityId = emailAuthenticationRepository.save(emailEntity).getId();
        String token = symmetricKeyEncoder.encrypt(String.valueOf(entityId));

        //When
        String result = emailService.authenticateEmail(token);

        //Then
        Assertions.assertEquals(EmailResponse.EMAIL_AUTHENTICATION_SUCCESS.getMessage(), result);
    }
}
