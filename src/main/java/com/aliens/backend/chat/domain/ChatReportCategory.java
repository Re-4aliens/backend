package com.aliens.backend.chat.domain;

import java.util.Optional;

public enum ChatReportCategory {
    SEXUAL_HARASSMENT, VIOLENCE, SPAM, SCAM, ETC;

    public static Optional<ChatReportCategory> fromString(String category) {
        for (ChatReportCategory reportCategory : ChatReportCategory.values()) {
            if (reportCategory.name().equalsIgnoreCase(category)) {
                return Optional.of(reportCategory);
            }
        }
        return Optional.empty();
    }
}