package com.aliens.backend.chat.service;

import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.global.error.ChatError;
import com.aliens.backend.global.exception.RestApiException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatAuthValidator {
    public void validateRoom(String topic, List<ChatRoom> validChatRooms) {
        Long roomId = getRoomIdFromTopic(topic);
        if(!isValidRoom(validChatRooms, roomId)) {
            throw new RestApiException(ChatError.INVALID_ROOM_ACCESS);
        }
    }

    private boolean isValidRoom (List<ChatRoom> validChatRooms, Long roomId) {
        return validChatRooms.stream().anyMatch(chatRoom -> chatRoom.getId().equals(roomId));
    }

    private long getRoomIdFromTopic(String topic) {
        return Long.parseLong(topic.split("/")[1]);
    }
}
