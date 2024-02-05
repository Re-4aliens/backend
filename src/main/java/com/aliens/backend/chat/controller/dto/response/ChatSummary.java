package com.aliens.backend.chat.controller.dto.response;

import com.aliens.backend.chat.domain.Message;

public record ChatSummary(Long roomId, Long partnerId, Message lastMessage, Long numberOfUnreadMessages) {
}
