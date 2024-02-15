package com.aliens.backend.matching.util.time;

import java.time.LocalDateTime;

public enum MockTime {
    INVALID_RECEPTION_TIME(LocalDateTime.of(2024, 1, 29, 19, 0)),
    VALID_RECEPTION_TIME_ON_MONDAY(LocalDateTime.of(2024, 1, 29, 10, 0)),
    VALID_RECEPTION_TIME_ON_THURSDAY(LocalDateTime.of(2024, 2, 1, 10, 0)),


    MONDAY(LocalDateTime.of(2024, 1, 29, 0, 0)),
    THURSDAY(LocalDateTime.of(2024, 2, 1, 0, 0)),
    ;

    private final LocalDateTime time;

    MockTime(LocalDateTime time) {
        this.time = time;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
