package com.aliens.backend.global.aspect.log.auth;

import com.aliens.backend.auth.controller.dto.AuthToken;
import com.aliens.backend.auth.controller.dto.LoginRequest;
import com.aliens.backend.global.response.log.InfoLogResponse;
import com.aliens.backend.global.response.success.AuthSuccess;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthLogAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;

    public AuthLogAspect(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.auth.pointcut.AuthPointCut.login() " +
            "&& args(loginRequest)")
    public void logLogin(LoginRequest loginRequest) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(AuthSuccess.GENERATE_TOKEN_SUCCESS, loginRequest.email());
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.auth.pointcut.AuthPointCut.reissue() " +
            "&& args(authToken)")
    public void logReissue(AuthToken authToken) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(AuthSuccess.REISSUE_TOKEN_SUCCESS, authToken);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.auth.pointcut.AuthPointCut.logout() " +
            "&& args(authToken)")
    public void logLogout(AuthToken authToken) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(AuthSuccess.LOGOUT_SUCCESS, authToken);
        log.info(objectMapper.writeValueAsString(response));
    }
}
