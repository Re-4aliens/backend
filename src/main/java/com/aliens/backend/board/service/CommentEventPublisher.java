package com.aliens.backend.board.service;

import com.aliens.backend.board.controller.dto.request.ChildCommentCreateRequest;
import com.aliens.backend.board.controller.dto.request.ParentCommentCreateRequest;
import com.aliens.backend.board.domain.Board;
import com.aliens.backend.notification.controller.dto.NotificationRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public CommentEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void sendParentCommentNotification(Board board,
                                              ParentCommentCreateRequest request) {

        eventPublisher.publishEvent(new NotificationRequest(
                board.getCategory(),
                board.getId(),
                "\"" + request.content() + "\" 라는 댓글이 달렸습니다.",
                List.of(board.getWriterId())));
    }

    public void sendChildCommentNotification(Board board,
                                              ChildCommentCreateRequest request,
                                              Long parentCommentMemberId) {
        System.out.println("작성자 Id" + board.getWriterId());
        System.out.println("부모댓글 Id" + parentCommentMemberId);
        eventPublisher.publishEvent(new NotificationRequest(
                board.getCategory(),
                board.getId(),
                "\"" + request.content() + "\" 라는 댓글이 달렸습니다.",
                List.of(board.getWriterId(), parentCommentMemberId)));
    }
}
