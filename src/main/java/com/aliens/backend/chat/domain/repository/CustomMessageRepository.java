package com.aliens.backend.chat.domain.repository;

import com.aliens.backend.chat.domain.Message;
import com.aliens.backend.chat.service.model.ChatMessageSummary;

import java.util.List;

public interface CustomMessageRepository {
    void markMessagesAsRead(Long chatRoomId, Long readBy);
    List<Message> findMessages(Long chatRoomId, String lastMessageId);
    List<ChatMessageSummary> aggregateMessageSummaries(List<Long> chatRoomIds, Long memberId);
}