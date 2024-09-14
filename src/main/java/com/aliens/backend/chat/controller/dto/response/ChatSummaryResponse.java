package com.aliens.backend.chat.controller.dto.response;

import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.domain.model.ChatMessageSummary;

import java.util.List;

public record ChatSummaryResponse(List<ChatRoom> chatRooms, List<ChatMessageSummary> chatMessageSummaries) {
}