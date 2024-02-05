package com.aliens.backend.chat.controller.dto.request;

public record MessageSendRequest(String type, String content, Long roomId, Long senderId, Long receiverId) {
}