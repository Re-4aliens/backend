package com.aliens.backend.email.service;

import com.aliens.backend.global.property.EmailProperties;
import com.aliens.backend.global.response.error.EmailError;
import com.aliens.backend.global.response.log.InfoLogResponse;
import com.aliens.backend.global.response.success.EmailSuccess;
import com.aliens.backend.member.controller.dto.event.TemporaryPasswordEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class EmailSender {

    private final EmailContent emailContent;
    private final EmailProperties emailProperties;
    private final ObjectMapper objectMapper;
    private final JavaMailSender javaMailSender;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public EmailSender(final EmailContent emailContent,
                       final EmailProperties emailProperties,
                       final ObjectMapper objectMapper,
                       final JavaMailSender javaMailSender) {
        this.emailContent = emailContent;
        this.emailProperties = emailProperties;
        this.objectMapper = objectMapper;
        this.javaMailSender = javaMailSender;
    }

    public void sendAuthenticationEmail(final String email, final String emailToken) {
        SimpleMailMessage mailMessage = createAuthenticationMail(email, emailToken);
        try {
            javaMailSender.send(mailMessage);

            InfoLogResponse response = InfoLogResponse.from(EmailSuccess.SEND_EMAIL_SUCCESS, email);
            log.info(objectMapper.writeValueAsString(response));
        } catch (Exception exception) {
            Map<String, String> data = new LinkedHashMap<>();
            data.put("sendTo", email);
            data.put("emailToken", emailToken);
            InfoLogResponse response = InfoLogResponse.from(EmailError.FAILED_TO_SEND_EMAIL, data);
            try {
                log.error(objectMapper.writeValueAsString(response));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
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
