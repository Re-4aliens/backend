package com.aliens.backend.board.controller.dto.request;

public record ParentCommentCreateRequest(Long boardId,
                                         String content) {
}
