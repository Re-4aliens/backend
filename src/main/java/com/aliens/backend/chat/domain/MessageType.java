package com.aliens.backend.chat.domain;

import java.util.Optional;

public enum MessageType {
    NORMAL, BALANCE_GAME;

    public static Optional<MessageType> fromString(String type) {
        for (MessageType messageType : MessageType.values()) {
            if (messageType.name().equalsIgnoreCase(type)) {
                return Optional.of(messageType);
            }
        }
        return Optional.empty();
    }
}
