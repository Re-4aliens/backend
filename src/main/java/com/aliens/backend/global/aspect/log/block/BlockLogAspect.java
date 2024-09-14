package com.aliens.backend.global.aspect.log.block;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.block.controller.dto.BlockRequest;
import com.aliens.backend.global.response.log.InfoLogResponse;
import com.aliens.backend.global.response.success.ChatSuccess;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Aspect
@Component
public class BlockLogAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;

    public BlockLogAspect(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.block.pointcut.BlockPointcut.blockPartner() " +
            "&& args(loginMember, blockRequest)", argNames = "loginMember,blockRequest")
    public void logBlockPartner(LoginMember loginMember, BlockRequest blockRequest) throws JsonProcessingException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("loginMember", loginMember);
        data.put("blockRequest", blockRequest);
        InfoLogResponse response = InfoLogResponse.from(ChatSuccess.BLOCK_SUCCESS, data);
        log.info(objectMapper.writeValueAsString(response));
    }
}
