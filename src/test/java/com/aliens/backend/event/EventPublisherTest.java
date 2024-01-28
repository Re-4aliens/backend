package com.aliens.backend.event;

import com.aliens.backend.notification.FcmSender;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
class EventPublisherTest {

    @Autowired
    ApplicationEventPublisher publisher;

    @MockBean
    FcmSender fcmSender;

    String givenType = "normal";
    String giveContent = "content";
    String givenToken = "token";

    @Test
    void listenSingleMessageRequestTest() {
        Message givenSingleMessage = Message.builder()
                .putData("type", givenType)
                .putData("comment", giveContent)
                .setToken(givenToken)
                .build();

        publisher.publishEvent(givenSingleMessage);

        verify(fcmSender, times(1)).listenSingleMessageRequest(any());
    }

    @Test
    void listenMultiMessageRequestTest() {
        MulticastMessage givenSingleMessage = MulticastMessage.builder()
                .putData("type", givenType)
                .putData("comment", giveContent)
                .addAllTokens(List.of(givenToken, givenToken))
                .build();

        publisher.publishEvent(givenSingleMessage);

        verify(fcmSender, times(1)).listenMultiMessageRequest(any());
    }
}
