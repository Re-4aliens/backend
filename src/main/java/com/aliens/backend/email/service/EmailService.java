package com.aliens.backend.email.service;

import com.aliens.backend.email.controller.response.EmailResponse;
import com.aliens.backend.email.domain.EmailAuthentication;
import com.aliens.backend.email.domain.repository.EmailAuthenticationRepository;
import com.aliens.backend.global.encode.SymmetricKeyEncoder;
import com.aliens.backend.global.error.EmailError;
import com.aliens.backend.global.exception.RestApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EmailService {

    private final EmailAuthenticationRepository emailAuthenticationRepository;
    private final EmailSender emailSender;
    private final SymmetricKeyEncoder symmetricKeyEncoder;

    public EmailService(final EmailAuthenticationRepository emailRepository,
                        final EmailSender emailSender,
                        final SymmetricKeyEncoder symmetricKeyEncoder) {
        this.emailAuthenticationRepository = emailRepository;
        this.emailSender = emailSender;
        this.symmetricKeyEncoder = symmetricKeyEncoder;
    }

    @Transactional(readOnly = true)
    public String duplicateCheck(final String email) {
        Optional<EmailAuthentication> emailAuthentication = emailAuthenticationRepository.findByEmail(email);

        if (emailAuthentication.isPresent()) {
            return EmailResponse.DUPLICATED_EMAIL.getMessage();
        }
        return EmailResponse.AVAILABLE_EMAIL.getMessage();
    }

    @Transactional
    public String sendAuthenticationEmail(final String email) throws Exception {
        EmailAuthentication emailEntity = new EmailAuthentication(email);
        emailAuthenticationRepository.save(emailEntity);

        String emailToken = symmetricKeyEncoder.encrypt(String.valueOf(emailEntity.getId()));
        emailSender.sendAuthenticationEmail(email, emailToken);

        return EmailResponse.EMAIL_SEND_SUCCESS.getMessage();
    }

    @Transactional
    public String authenticateEmail(final String token) throws Exception {
        Long emailEntityId = Long.valueOf(symmetricKeyEncoder.decrypt(token));
        EmailAuthentication emailEntity = getEmailAuthentication(emailEntityId);
        emailEntity.authenticate();
        return EmailResponse.EMAIL_AUTHENTICATION_SUCCESS.getMessage();
    }

    private EmailAuthentication getEmailAuthentication(final Long emailEntityId) {
        return emailAuthenticationRepository.findById(emailEntityId).orElseThrow(() -> new RestApiException(EmailError.NULL_EMAIL));
    }
}
