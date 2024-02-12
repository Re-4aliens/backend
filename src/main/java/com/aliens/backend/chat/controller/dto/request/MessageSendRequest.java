package com.aliens.backend.chat.controller.dto.request;

import com.aliens.backend.chat.domain.MessageType;

public record MessageSendRequest(MessageType type, String content, Long roomId, Long senderId, Long receiverId) {
}