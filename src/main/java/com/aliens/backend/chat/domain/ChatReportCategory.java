package com.aliens.backend.chat.domain;

import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.ChatError;

public enum ChatReportCategory {
    SEXUAL_HARASSMENT, VIOLENCE, SPAM, SCAM, ETC;

    public static ChatReportCategory fromString(String category) {
        for (ChatReportCategory reportCategory : ChatReportCategory.values()) {
            if (reportCategory.name().equalsIgnoreCase(category)) {
                return reportCategory;
            }
        }
        throw new RestApiException(ChatError.INVALID_REPORT_CATEGORY);
    }
}