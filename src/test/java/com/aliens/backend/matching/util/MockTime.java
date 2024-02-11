package com.aliens.backend.matching.util;

import java.time.LocalDateTime;

public enum MockTime {
    INVALID_TIME(LocalDateTime.of(2024, 1, 29, 19, 0)),
    VALID_TIME(LocalDateTime.of(2024, 1, 29, 10, 0)),

    MONDAY(LocalDateTime.of(2024, 1, 29, 0, 0)),
    THURSDAY(LocalDateTime.of(2024, 2, 1, 0, 0)),
    ;

    public LocalDateTime time;

    private MockTime(LocalDateTime time) {
        this.time = time;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
