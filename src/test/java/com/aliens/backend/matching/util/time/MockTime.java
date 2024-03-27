package com.aliens.backend.matching.util.time;

import java.time.LocalDateTime;

public enum MockTime {
    INVALID_RECEPTION_TIME(LocalDateTime.of(2024, 2, 15, 22, 0)),
    VALID_RECEPTION_TIME_ON_TUESDAY(LocalDateTime.of(2024, 2, 13, 10, 0)),
    VALID_RECEPTION_TIME_ON_FRIDAY(LocalDateTime.of(2024, 2, 16, 10, 0)),

    MATCHING_EXPIRATION_TIME_ON_THURSDAY(LocalDateTime.of(2024, 2, 15, 21, 0)),
    MATCHING_EXPIRATION_TIME_ON_MONDAY(LocalDateTime.of(2024, 2, 19, 21, 0)),

    TUESDAY(LocalDateTime.of(2024, 2, 13, 0, 0)),
    FRIDAY(LocalDateTime.of(2024, 2, 16, 0, 0)),
    ;

    private final LocalDateTime time;

    MockTime(LocalDateTime time) {
        this.time = time;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
