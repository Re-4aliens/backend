package com.aliens.backend.chat.controller.dto.request;

public record ChatReportRequest(Long partnerId, Long chatRoomId, String category, String content) {
}