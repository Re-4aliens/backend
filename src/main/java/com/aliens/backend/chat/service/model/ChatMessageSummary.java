package com.aliens.backend.chat.service.model;

public record ChatMessageSummary(Long roomId, String lastMessageContent, Long numberOfUnreadMessages) {
}