package com.aliens.backend.event;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.chat.controller.dto.event.ChatRoomCreationEvent;
import com.aliens.backend.chat.service.ChatService;
import com.aliens.backend.chat.service.model.MemberPair;
import com.aliens.backend.global.BaseServiceTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.notification.FcmSender;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class EventPublisherTest extends BaseServiceTest {

    @Autowired ApplicationEventPublisher publisher;
    @Autowired FcmSender fcmSender;
    @SpyBean ChatService chatService;
    @Autowired DummyGenerator dummyGenerator;

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

    @Test
    @DisplayName("채팅방 생성 이벤트 발행 및 처리")
    void handleChatRoomCreationEventTest() {
        // Given
        Set<MemberPair> matchedPairs = generateMultiMemberPair(5);
        ChatRoomCreationEvent event = new ChatRoomCreationEvent(matchedPairs);

        // When
        publisher.publishEvent(event);

        // Then
        verify(chatService, times(1)).handleChatRoomCreationEvent(event);
    }

    private Set<MemberPair> generateMultiMemberPair(Integer memberCount) {
        List<Member> members = dummyGenerator.generateMultiMember(memberCount);
        Set<MemberPair> memberPairs = new HashSet<>();
        for (int i = 0; i < members.size(); i++) {
            for (int j = 0; j < members.size(); j++) {
                if (i != j) {
                    memberPairs.add(new MemberPair(members.get(i), members.get(j)));
                }
            }
        }
        return memberPairs;
    }
}
