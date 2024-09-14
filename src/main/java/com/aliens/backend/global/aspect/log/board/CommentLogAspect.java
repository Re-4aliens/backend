package com.aliens.backend.global.aspect.log.board;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.board.controller.dto.request.ChildCommentCreateRequest;
import com.aliens.backend.board.controller.dto.request.ParentCommentCreateRequest;
import com.aliens.backend.global.response.log.InfoLogResponse;
import com.aliens.backend.global.response.success.CommentSuccess;
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
public class CommentLogAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;

    public CommentLogAspect(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.CommentPointcut.createParentComment() " +
            "&& args(request, loginMember)", argNames = "request,loginMember")
    public void logCreateParentComment(ParentCommentCreateRequest request, LoginMember loginMember) throws JsonProcessingException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("loginMember", loginMember);
        data.put("parentCommentCreateRequest", request);
        InfoLogResponse response = InfoLogResponse.from(CommentSuccess.PARENT_COMMENT_CREATE_SUCCESS, data);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.CommentPointcut.createChildComment() " +
            "&& args(request, loginMember)", argNames = "request,loginMember")
    public void logCreateChildComment(ChildCommentCreateRequest request, LoginMember loginMember) throws JsonProcessingException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("loginMember", loginMember);
        data.put("childCommentCreateRequest", request);
        InfoLogResponse response = InfoLogResponse.from(CommentSuccess.CHILD_COMMENT_CREATE_SUCCESS, data);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.CommentPointcut.getPageMyCommentedBoards() " +
            "&& args(loginMember, ..)")
    public void logGetPageMyCommentedBoards(LoginMember loginMember) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(CommentSuccess.GET_MY_COMMENTED_BOARD_PAGE_SUCCESS, loginMember);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.CommentPointcut.getCommentsByBoardId() " +
            "&& args(boardId)")
    public void logGetCommentsByBoardId(Long boardId) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(CommentSuccess.GET_COMMENTS_BY_BOARD_ID_SUCCESS, boardId);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.board.pointcut.CommentPointcut.deleteComment() " +
            "&& args(loginMember, commentId)", argNames = "loginMember,commentId")
    public void logDeleteComment(LoginMember loginMember, Long commentId) throws JsonProcessingException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("loginMember", loginMember);
        data.put("commentId", commentId);
        InfoLogResponse response = InfoLogResponse.from(CommentSuccess.DELETE_COMMENT_SUCCESS, data);
        log.info(objectMapper.writeValueAsString(response));
    }
}
