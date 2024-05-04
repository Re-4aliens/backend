package com.aliens.backend.chat.domain;

import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    @DisplayName("Message 생성 테스트")
    void CreateMessage() {
        // given
        MessageSendRequest messageSendRequest = new MessageSendRequest(
                MessageType.NORMAL,
                "content",
                1L,
                1L,
                2L
        );

        // when
        Message message = Message.of(messageSendRequest);

        // then
        assertNotNull(message);
    }

    @Test
    @DisplayName("Message toString 테스트")
    void ToString() {
        // given
        MessageSendRequest messageSendRequest = new MessageSendRequest(
                MessageType.NORMAL,
                "content",
                1L,
                1L,
                2L
        );
        Message message = Message.of(messageSendRequest);

        // when
        String result = message.toString();

        // then
        assertTrue(result.contains("id"));
        assertTrue(result.contains("type"));
        assertTrue(result.contains("content"));
        assertTrue(result.contains("roomId"));
        assertTrue(result.contains("senderId"));
        assertTrue(result.contains("receiverId"));
        assertTrue(result.contains("sendTime"));
        assertTrue(result.contains("isRead"));
    }
}