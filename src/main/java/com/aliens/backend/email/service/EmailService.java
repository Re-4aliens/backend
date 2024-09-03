package com.aliens.backend.email.service;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.email.controller.response.EmailResponse;
import com.aliens.backend.email.domain.EmailAuthentication;
import com.aliens.backend.email.domain.repository.EmailAuthenticationRepository;
import com.aliens.backend.member.sevice.SymmetricKeyEncoder;
import com.aliens.backend.global.response.error.EmailError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.member.controller.dto.event.TemporaryPasswordEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EmailService {

    private final EmailAuthenticationRepository emailAuthenticationRepository;
    private final EmailSender emailSender;
    private final MemberRepository memberRepository;

    public EmailService(final EmailAuthenticationRepository emailRepository,
                        final EmailSender emailSender,
                        final MemberRepository memberRepository) {
        this.emailAuthenticationRepository = emailRepository;
        this.emailSender = emailSender;
        this.memberRepository = memberRepository;
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
    public String sendAuthenticationEmail(final String email) {
        deleteExistsEmail(email);
        checkAlreadyMember(email);

        EmailAuthentication emailEntity = new EmailAuthentication(email);
        emailAuthenticationRepository.save(emailEntity);

        String emailToken = SymmetricKeyEncoder.encrypt(email);
        emailSender.sendAuthenticationEmail(email, emailToken);

        return EmailResponse.EMAIL_SEND_SUCCESS.getMessage();
    }

    private void checkAlreadyMember(final String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isPresent()) {
            throw new RestApiException(EmailError.ALREADY_MEMBER);
        }
    }

    private void deleteExistsEmail(final String email) {
        Optional<EmailAuthentication> checkEntity = emailAuthenticationRepository.findByEmail(email);
        checkEntity.ifPresent(emailAuthentication -> emailAuthenticationRepository.deleteById(emailAuthentication.getId()));
    }

    @Transactional
    public String authenticateEmail(final String token) {
        String email = SymmetricKeyEncoder.decrypt(token);
        EmailAuthentication emailEntity = getEmailAuthenticationByEmail(email);
        emailEntity.authenticate();
        return EmailResponse.EMAIL_AUTHENTICATION_SUCCESS.getMessage();
    }

    private EmailAuthentication getEmailAuthenticationByEmail(final String email) {
        return emailAuthenticationRepository.findByEmail(email).orElseThrow(() -> new RestApiException(EmailError.NULL_EMAIL));
    }

    @EventListener
    public void listen(TemporaryPasswordEvent event) {
        emailSender.sendTemporaryPassword(event);
    }

    public String checkEmailAuthenticated(final String email) {
        EmailAuthentication emailAuthentication = findByEmail(email);

        if(emailAuthentication.isAuthenticated()) {
            return EmailResponse.CAN_NEXT_STEP.getMessage();
        }
        return EmailResponse.CANT_NEXT_STEP.getMessage();
    }

    private EmailAuthentication findByEmail(final String email) {
        return emailAuthenticationRepository.findByEmail(email).orElseThrow(() -> new RestApiException(EmailError.NULL_EMAIL));
    }
}
