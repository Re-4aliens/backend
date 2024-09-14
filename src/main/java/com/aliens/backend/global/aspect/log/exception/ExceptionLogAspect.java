package com.aliens.backend.global.aspect.log.exception;

import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.log.InfoLogResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionLogAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;

    public ExceptionLogAspect(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.exception.pointcut.ExceptionPointcut.exceptionHandler() " +
            "&& args(apiException)")
    public void logExceptionHandler(RestApiException apiException) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(apiException.getErrorCode());
        log.info(objectMapper.writeValueAsString(response));
    }
}
