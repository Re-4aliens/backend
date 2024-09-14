package com.aliens.backend.global.aspect.log.board;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.board.controller.dto.request.MarketBoardCreateRequest;
import com.aliens.backend.board.controller.dto.request.MarketChangeRequest;
import com.aliens.backend.global.response.log.InfoLogResponse;
import com.aliens.backend.global.response.success.GreatSuccess;
import com.aliens.backend.global.response.success.MarketBoardSuccess;
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
public class MarketLogAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;

    public MarketLogAspect(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.MarketPointcut.createMarketBoard() " +
            "&& args(loginMember, request, ..)", argNames = "loginMember,request")
    public void logCreateMarketBoard(LoginMember loginMember, MarketBoardCreateRequest request) throws JsonProcessingException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("loginMember", loginMember);
        data.put("boardTitle", request.title());
        InfoLogResponse response = InfoLogResponse.from(MarketBoardSuccess.CREATE_MARKET_BOARD_SUCCESS, data);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.MarketPointcut.getMarketBoardPage())")
    public void logGetMarketBoardPage() throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(MarketBoardSuccess.GET_MARKET_BOARD_PAGE_SUCCESS);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.MarketPointcut.getMarketBoardDetails() " +
            "&& args(boardId)")
    public void logGetMarketBoardDetails(Long boardId) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(MarketBoardSuccess.GET_MARKET_BOARD_DETAILS_SUCCESS, boardId);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.MarketPointcut.searchMarketBoards() " +
            "&& args(searchKeyword, ..)")
    public void logSearchMarketBoards(String searchKeyword) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(MarketBoardSuccess.SEARCH_MARKET_BOARD_SUCCESS, searchKeyword);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.MarketPointcut.changeMarketBoard() " +
            "&& args(loginMember, boardId, request)", argNames = "loginMember,boardId,request")
    public void logChangeMarketBoard(LoginMember loginMember, Long boardId, MarketChangeRequest request) throws JsonProcessingException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("loginMember", loginMember);
        data.put("boardId", boardId);
        data.put("marketChangeRequest", request);
        InfoLogResponse response = InfoLogResponse.from(MarketBoardSuccess.CHANGE_MARKET_BOARD_SUCCESS, data);
        log.info(objectMapper.writeValueAsString(response));
    }
}
