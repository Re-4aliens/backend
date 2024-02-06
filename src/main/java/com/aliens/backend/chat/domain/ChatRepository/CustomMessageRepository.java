package com.aliens.backend.chat.domain.ChatRepository;

import com.aliens.backend.chat.domain.Message;

import java.util.List;

public interface CustomMessageRepository {
    void markMessagesAsRead(Long chatRoomId, Long readBy);
    List<Message> findMessages(Long chatRoomId, String lastMessageId);
}