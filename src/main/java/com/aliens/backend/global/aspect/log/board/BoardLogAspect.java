package com.aliens.backend.global.aspect.log.board;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.board.controller.dto.request.BoardCreateRequest;
import com.aliens.backend.global.response.log.InfoLogResponse;
import com.aliens.backend.global.response.success.BoardSuccess;
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
public class BoardLogAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;

    public BoardLogAspect(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.BoardPointcut.createBoard() " +
            "&& args(loginMember, request, ..)", argNames = "loginMember,request")
    public void logCreateBoard(LoginMember loginMember, BoardCreateRequest request) throws JsonProcessingException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("loginMember", loginMember);
        data.put("boardTitle", request.title());
        InfoLogResponse response = InfoLogResponse.from(BoardSuccess.POST_BOARD_SUCCESS, data);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.BoardPointcut.getSingleBoard() " +
            "&& args(id)")
    public void logGetSingleBoard(Long id) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(BoardSuccess.GET_SINGLE_BOARD_SUCCESS, id);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.BoardPointcut.getAllBoards())")
    public void logGetAllBoards() throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(BoardSuccess.GET_ALL_BOARDS_SUCCESS);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.BoardPointcut.getAllBoardsWithCategory() " +
            "&& args(category, ..)", argNames = "category")
    public void logGetAllBoardsWithCategory(String category) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(BoardSuccess.GET_ALL_BOARDS_WITH_CATEGORY_SUCCESS, category);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.BoardPointcut.searchAllBoardPage() " +
            "&& args(searchKeyword, ..)", argNames = "searchKeyword")
    public void logSearchAllBoardPage(String searchKeyword) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(BoardSuccess.SEARCH_ALL_BOARDS_SUCCESS, searchKeyword);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.BoardPointcut.getAllAnnouncementBoards())")
    public void logGetAllAnnouncementBoards() throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(BoardSuccess.GET_ALL_ANNOUNCEMENT_BOARDS_SUCCESS);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.BoardPointcut.getPageMyBoards() " +
            "&& args(loginMember, ..)")
    public void logGetPageMyBoards(LoginMember loginMember) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(BoardSuccess.GET_MY_BOARD_PAGE_SUCCESS, loginMember);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.BoardPointcut.searchBoardsWithCategory() " +
            "&& args(searchKeyword, category, ..)", argNames = "searchKeyword,category")
    public void logSearchBoardsWithCategory(String searchKeyword, String category) throws JsonProcessingException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("searchKeyword", searchKeyword);
        data.put("category", category);
        InfoLogResponse response = InfoLogResponse.from(BoardSuccess.SEARCH_BOARD_WITH_CATEGORY_SUCCESS, data);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.BoardPointcut.deleteBoard() " +
            "&& args(loginMember, id)", argNames = "loginMember,id")
    public void logDeleteBoard(LoginMember loginMember, Long id) throws JsonProcessingException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("loginMember", loginMember);
        data.put("boardId", id);
        InfoLogResponse response = InfoLogResponse.from(BoardSuccess.DELETE_BOARD_SUCCESS, data);
        log.info(objectMapper.writeValueAsString(response));
    }
}
