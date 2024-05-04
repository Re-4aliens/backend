package com.aliens.backend.notification.controller.dto;

import com.aliens.backend.board.domain.enums.BoardCategory;
import com.aliens.backend.notification.domain.NotificationType;

import java.time.Instant;

public record NotificationResponse(
        Long id,
        String content,
        Long boardId,
        NotificationType noticeType,
        BoardCategory category,
        Instant createdAt,
        Boolean isRead) {
}
