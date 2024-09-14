package com.aliens.backend.chat.domain.model;

import java.util.Date;

public record ChatMessageSummary(Long roomId, String lastMessageContent, Date lastMessageTime, Long numberOfUnreadMessages) {
}