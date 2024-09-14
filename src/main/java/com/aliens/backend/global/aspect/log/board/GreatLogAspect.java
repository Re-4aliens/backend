package com.aliens.backend.global.aspect.log.board;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.board.controller.dto.request.BoardCreateRequest;
import com.aliens.backend.global.response.log.InfoLogResponse;
import com.aliens.backend.global.response.success.BoardSuccess;
import com.aliens.backend.global.response.success.GreatSuccess;
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
public class GreatLogAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;

    public GreatLogAspect(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.GreatPointcut.greatAtBoard() " +
            "&& args(boardId, loginMember)", argNames = "boardId,loginMember")
    public void logGreatAtBoard(Long boardId, LoginMember loginMember) throws JsonProcessingException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("loginMember", loginMember);
        data.put("boardId", boardId);
        InfoLogResponse response = InfoLogResponse.from(GreatSuccess.GREAT_AT_BOARD_SUCCESS, data);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.GreatPointcut.getAllGreatBoards() " +
            "&& args(loginMember, ..)")
    public void logGetAllGreatBoards(LoginMember loginMember) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(GreatSuccess.GET_ALL_GREAT_BOARDS_SUCCESS, loginMember);
        log.info(objectMapper.writeValueAsString(response));
    }
}
