package com.aliens.backend.matching;

import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.validator.MatchingApplicationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

@Component
public class MockClock {
    @MockBean
    MatchingApplicationValidator matchingApplicationValidator;

    @MockBean
    private Clock clock;

    public void mockTime(MatchingRound matchingRound, MockTime mockTime) {
        LocalDateTime time = mockTime.time;
        Clock fixedClock = Clock.fixed(time.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());

        doCallRealMethod().when(matchingApplicationValidator).checkReceptionTime(matchingRound, time);
    }
}
