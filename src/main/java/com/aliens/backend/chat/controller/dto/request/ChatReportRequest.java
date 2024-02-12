package com.aliens.backend.chat.controller.dto.request;

import com.aliens.backend.chat.domain.ChatReportCategory;

public record ChatReportRequest(Long partnerId, Long chatRoomId, ChatReportCategory category, String content) {
}