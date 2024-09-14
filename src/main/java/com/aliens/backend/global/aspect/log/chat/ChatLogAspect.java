package com.aliens.backend.global.aspect.log.chat;

import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import com.aliens.backend.global.response.log.InfoLogResponse;
import com.aliens.backend.global.response.success.ChatSuccess;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ChatLogAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;

    public ChatLogAspect(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.chat.pointcut.ChatPointcut.sendMessage() " +
            "&& args(messageSendRequest)")
    public void logSendMessage(MessageSendRequest messageSendRequest) throws JsonProcessingException {
        InfoLogResponse response =
                InfoLogResponse.from(ChatSuccess.SEND_MESSAGE_SUCCESS, messageSendRequest);
        log.info(objectMapper.writeValueAsString(response));
    }
}
