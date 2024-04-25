package com.aliens.backend.board.controller.dto.request;

public record ReportBoardRequest(Long boardId,
                                 String reason) {
}
