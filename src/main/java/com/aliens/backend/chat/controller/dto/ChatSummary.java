package com.aliens.backend.chat.controller.dto;

import com.aliens.backend.chat.domain.Message;

public record ChatSummary(Long roomId, Long partnerId, Message lastMessage, Long numberOfUnreadMessages) {
}
