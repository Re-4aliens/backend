package com.aliens.backend.event;

import com.aliens.backend.global.BaseTest;
import com.aliens.backend.notification.FcmSender;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
class EventPublisherTest extends BaseTest {

    @Autowired
    ApplicationEventPublisher publisher;

    @Autowired
    FcmSender fcmSender;

    String givenType = "normal";
    String giveContent = "content";
    String givenToken = "token";

    @Test
    @DisplayName("단일 메시지 이벤트 발행 및 전송 ")
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
    @DisplayName("다수 메시지 이벤트 발행 및 전송 ")
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
