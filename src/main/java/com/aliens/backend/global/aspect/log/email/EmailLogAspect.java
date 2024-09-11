package com.aliens.backend.global.aspect.log.email;

import com.aliens.backend.global.response.log.InfoLogResponse;
import com.aliens.backend.global.response.success.EmailSuccess;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EmailLogAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;

    public EmailLogAspect(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.email.pointcut.EmailPointcut.duplicateCheck() " +
            "&& args(email)")
    public void logDuplicateCheck(String email) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(EmailSuccess.DUPLICATE_CHECK_SUCCESS, email);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.email.pointcut.EmailPointcut.sendAuthenticationEmail() " +
            "&& args(email)")
    public void logSendAuthenticationEmail(String email) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(EmailSuccess.SEND_EMAIL_SUCCESS, email);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.email.pointcut.EmailPointcut.authenticateEmail() " +
            "&& args(token)")
    public void logAuthenticateEmail(String token) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(EmailSuccess.DUPLICATE_CHECK_SUCCESS, token);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.email.pointcut.EmailPointcut.checkEmailAuthenticated() " +
            "&& args(email)")
    public void logCheckEmailAuthenticated(String email) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(EmailSuccess.EMAIL_AUTHENTICATE_SUCCESS, email);
        log.info(objectMapper.writeValueAsString(response));
    }

}
