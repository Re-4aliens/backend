package com.aliens.backend.email.service;

import com.aliens.backend.global.property.EmailProperties;
import com.aliens.backend.member.controller.dto.event.TemporaryPasswordEvent;
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

    public void sendAuthenticationEmail(final String email, final String emailToken) {
        SimpleMailMessage mailMessage = createAuthenticationMail(email, emailToken);
        javaMailSender.send(mailMessage);
    }

    private SimpleMailMessage createAuthenticationMail(final String email, final String emailToken) {
        SimpleMailMessage authenticationEmail = new SimpleMailMessage();
        authenticationEmail.setTo(email);
        authenticationEmail.setSubject(emailContent.getAuthenticationMailTitle());

        String content = emailContent.getAuthenticationMailContent(emailToken, emailProperties.getDomainUrl());
        authenticationEmail.setText(content);
        return authenticationEmail;
    }

    public void sendTemporaryPassword(final TemporaryPasswordEvent event) {
        SimpleMailMessage mailMessage = createTemporaryPassword(event);
        javaMailSender.send(mailMessage);
    }

    private SimpleMailMessage createTemporaryPassword(final TemporaryPasswordEvent event) {
        SimpleMailMessage temporaryPasswordEmail = new SimpleMailMessage();
        temporaryPasswordEmail.setTo(event.email());
        temporaryPasswordEmail.setSubject(emailContent.getTemporaryMailTitle());

        String content = emailContent.getTemporaryMailContent(event.tmpPassword());
        temporaryPasswordEmail.setText(content);
        return temporaryPasswordEmail;
    }
}
