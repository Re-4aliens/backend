package com.aliens.backend.email.sender;

import com.aliens.backend.email.service.EmailSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
class EmailSenderTest {

    @Autowired
    EmailSender emailSender;

    @Test
    @DisplayName("인증 이메일 전송 테스트")
    void sendAuthenticationEmailTest() {
        //Given
        String email = "tmp@example.com";
        String token = "1";
        JavaMailSender javaMailSender = mock(JavaMailSender.class);
        emailSender.setJavaMailSender(javaMailSender);

        //When
        emailSender.sendAuthenticationEmail(email, token);

        //Then
        verify(javaMailSender).send(ArgumentMatchers.any(SimpleMailMessage.class));
    }
}
