package com.aliens.backend.board.controller.dto.request;

public record ChildCommentCreateRequest(Long boardId, String content, Long parentCommentId) {
}
