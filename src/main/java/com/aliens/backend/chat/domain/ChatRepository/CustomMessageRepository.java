package com.aliens.backend.chat.domain.ChatRepository;

public interface CustomMessageRepository {
    void markMessagesAsRead(Long chatRoomId, Long readBy);
}