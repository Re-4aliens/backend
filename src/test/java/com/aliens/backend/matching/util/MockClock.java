package com.aliens.backend.matching.util;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.mockito.Mockito.when;

@Component
public class MockClock {
    @MockBean
    private Clock clock;

    public void mockTime(MockTime mockTime) {
        LocalDateTime time = mockTime.time;
        Clock fixedClock = Clock.fixed(time.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());
    }
}
