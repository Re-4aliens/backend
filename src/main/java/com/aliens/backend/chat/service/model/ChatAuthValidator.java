package com.aliens.backend.chat.service.model;

import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.domain.ChatRoomStatus;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.ChatError;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatAuthValidator {
    public void validateRoomFromTopic(String topic, List<ChatRoom> validChatRooms) {
        Long roomId = getRoomIdFromTopic(topic);
        if(!isUserInRoom(validChatRooms, roomId)) {
            throw new RestApiException(ChatError.INVALID_ROOM_ACCESS);
        }
    }

    public void validateRoom(Long roomId, List<ChatRoom> validChatRooms) {
        if (!isUserInRoom(validChatRooms, roomId)) {
            throw new RestApiException(ChatError.INVALID_ROOM_ACCESS);
        }
        if (!isRoomOpened(validChatRooms, roomId)) {
            throw new RestApiException(ChatError.ROOM_NOT_OPEN);
        }
    }

    private boolean isUserInRoom(List<ChatRoom> validChatRooms, Long roomId) {
        return validChatRooms.stream().anyMatch(chatRoom -> chatRoom.getId().equals(roomId));
    }

    private boolean isRoomOpened(List<ChatRoom> validChatRooms, Long roomId) {
        return validChatRooms.stream()
                .filter(chatRoom -> chatRoom.getId().equals(roomId))
                .findFirst()
                .map(chatRoom -> chatRoom.getStatus().equals(ChatRoomStatus.OPENED))
                .orElse(false);
    }

    private long getRoomIdFromTopic(String topic) {
        return Long.parseLong(topic.split("/")[2]);
    }
}
