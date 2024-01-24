package com.aliens.backend.email.service;

import com.aliens.backend.global.property.EmailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    private final EmailContent emailContent;
    private final EmailProperties emailProperties;
    private JavaMailSender javaMailSender;

    public EmailSender(final EmailContent emailContent,
                       final EmailProperties emailProperties,
                       final JavaMailSender javaMailSender) {
        this.emailContent = emailContent;
        this.emailProperties = emailProperties;
        this.javaMailSender = javaMailSender;
    }

    public void setJavaMailSender(final JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendAuthenticationEmail(final String email, final String emailToken) {
        SimpleMailMessage mailMessage = createAuthenticationMail(email, emailToken);
        javaMailSender.send(mailMessage);
    }

    private SimpleMailMessage createAuthenticationMail(final String email, final String emailToken) {
        SimpleMailMessage authenticationEmail = new SimpleMailMessage();
        authenticationEmail.setTo(email);
        authenticationEmail.setSubject(emailContent.getTitle());

        String content = emailContent.getContent(emailToken, emailProperties.getDomainUrl());
        authenticationEmail.setText(content);
        return authenticationEmail;
    }
}
