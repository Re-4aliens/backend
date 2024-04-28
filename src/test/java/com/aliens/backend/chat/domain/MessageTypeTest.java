package com.aliens.backend.chat.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MessageTypeTest {
    @Test
    @DisplayName("MessageType fromString 테스트")
    void FromString() {
        // given
        String type = "NORMAL";

        // when
        Optional<MessageType> result = MessageType.fromString(type);

        // then
        assertEquals(MessageType.NORMAL, result.get());
    }

    @Test
    @DisplayName("MessageType fromString - 잘못된 type")
    void FromString_InvalidType() {
        // given
        String type = "INVALID";

        // when
        Optional<MessageType> result = MessageType.fromString(type);

        // then
        assertTrue(result.isEmpty());
    }
}